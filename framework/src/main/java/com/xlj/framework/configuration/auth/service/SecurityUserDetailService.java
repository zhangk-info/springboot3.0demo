package com.xlj.framework.configuration.auth.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xlj.common.constants.CacheConstants;
import com.xlj.common.constants.Constants;
import com.xlj.common.context.UserType;
import com.xlj.common.exception.ServiceException;
import com.xlj.common.utils.MessageUtils;
import com.xlj.framework.configuration.auth.common.SecurityUserDetails;
import com.xlj.framework.configuration.auth.exception.UserPasswordRetryLimitExceedException;
import com.xlj.framework.manager.AsyncManager;
import com.xlj.framework.manager.factory.AsyncFactory;
import com.xlj.system.configuration.RedisService;
import com.xlj.system.domain.entity.SysUser;
import com.xlj.system.domain.model.LoginUser;
import com.xlj.system.enums.UserStatus;
import com.xlj.system.service.ISysUserService;
import com.xlj.system.service.SysPermissionService;
import com.xlj.user.entity.User;
import com.xlj.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@Slf4j
public class SecurityUserDetailService implements UserDetailsService {

    /**
     * 禁用
     */
    public final static int ACCOUNT_STATUS_DISABLE = 0;
    /**
     * 正常
     */
    public final static int ACCOUNT_STATUS_NORMAL = 1;
    /**
     * 锁定
     */
    public final static int ACCOUNT_STATUS_LOCKED = 2;
    /**
     * 电话号码匹配正则表达式
     */
    private static final Pattern MOBILEW_PATTERN = Pattern.compile("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\\\d{8}$");
    private final int maxRetryCount = 5;
    private final int lockTime = 5;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (JSONUtil.isTypeJSON(username)) {
            // 如果是json表示是后台用户登录，走后台登录逻辑
            JSONObject jsonObject = JSONUtil.parseObj(username);
            return loadBySysUser(jsonObject.getStr("username"));
        } else {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.and(accountQueryWrapper -> {
                accountQueryWrapper.eq("mobile", username);
            });
            List<User> accounts = userMapper.selectList(wrapper);
            if (accounts == null || accounts.size() == 0) {
                throw new UsernameNotFoundException("");
            }
            return buildUserDetails(accounts.get(0));
        }
    }

    private SecurityUserDetails buildUserDetails(User user) {
        Long id = user.getId();
        String password = user.getPassword();
        String nickName = user.getNickname();
        boolean accountNonLocked = true;
        boolean credentialsNonExpired = true;
        boolean enabled = true;
        boolean accountNonExpired = true;
        //设置SecurityUserDetails
        SecurityUserDetails userDetails = new SecurityUserDetails();
        userDetails.setUserId(id);
        userDetails.setUsername(user.getMobile());
        userDetails.setPassword(password);
        userDetails.setNickName(nickName);
        //账号没有锁定
        userDetails.setAccountNonLocked(accountNonLocked);
        //账号没有过期
        userDetails.setAccountNonExpired(accountNonExpired);
        //账号证书没有过期
        userDetails.setCredentialsNonExpired(credentialsNonExpired);
        //账号启用
        userDetails.setEnabled(enabled);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserType.DEFAULT.toString()));
        userDetails.setAuthorities(authorities);
        return userDetails;
    }

    /**
     * 后台用户登录
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadBySysUser(String username) throws UsernameNotFoundException {
        SysUser user = sysUserService.selectUserByUserName(username);
        if (Objects.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException("登录用户：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }

        try {
            validate(user);
        } catch (Exception e) {
            log.info("登录用户：{} 登录失败次数超过限制.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 登录失败次数超过限制，请稍后重试");
        }

        return createLoginUser(user);
    }

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    /**
     * 登录次数限制验证
     *
     * @param user
     */
    private void validate(SysUser user) {
        String username = user.getUserName();
        Integer retryCount = (Integer) redisService.get(getCacheKey(username));

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount >= Integer.valueOf(maxRetryCount).intValue()) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount, lockTime)));
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        clearLoginRecordCache(username);
    }

    public void clearLoginRecordCache(String loginName) {
        if (redisService.hasKey(getCacheKey(loginName))) {
            redisService.del(getCacheKey(loginName));
        }
    }

    public UserDetails createLoginUser(SysUser user) {
        Set<String> permissions = permissionService.getMenuPermission(user);
        permissions.add(UserType.SYSTEM.toString());
        return new LoginUser(user, permissions);
    }

}
