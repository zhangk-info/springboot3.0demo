package com.xlj.system.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xlj.common.constants.Constants;
import com.xlj.common.exception.ServiceException;
import com.xlj.common.utils.MessageUtils;
import com.xlj.common.utils.ServletUtils;
import com.xlj.common.utils.ip.IpUtils;
import com.xlj.framework.captch.CaptchService;
import com.xlj.framework.manager.AsyncManager;
import com.xlj.framework.manager.factory.AsyncFactory;
import com.xlj.system.configuration.RedisService;
import com.xlj.system.domain.entity.SysUser;
import com.xlj.system.domain.model.LoginUser;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService {

    @Autowired
    private RedisService redisCache;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysConfigService configService;

    @Value("${oauth2.token.uri}")
    private String tokenUri;

    @Resource
    private JwtDecoder jwtDecoder;

    @Autowired
    private CaptchService captchService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @param request
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid, HttpServletRequest request) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关
        if (captchaEnabled) {
            captchService.validateCaptcha(username, code, uuid);
        }

        Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("grant_type", "password");
        loginParams.put("username", "{\"username\":\"" + username + "\"}");
        loginParams.put("password", 123456);
        loginParams.put("client_id", "password");
        loginParams.put("client_secret", "xinglianjing");
        StringBuilder cookieStr = new StringBuilder(StringUtils.EMPTY);
        for (Cookie cookie : request.getCookies()) {
            cookieStr.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }
        HttpRequest httpRequest = HttpUtil.createRequest(Method.POST, tokenUri).form(loginParams).cookie(cookieStr.toString());
        String res = httpRequest.timeout(HttpGlobalConfig.getTimeout()).execute().body();
        JSONObject resData = JSONUtil.parseObj(res);
        String access_token = resData.getStr("access_token");
        if (StringUtils.isNotBlank(access_token)) {
            // 成功会返回token
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
            Jwt jwt = jwtDecoder.decode(access_token);
            LoginUser loginUser = JSONUtil.toBean(JSONUtil.toJsonStr(jwt.getClaims()), JSONConfig.create().setIgnoreError(true), LoginUser.class);
            recordLoginInfo(loginUser.getUserId());
            // 生成token
            return access_token;
        } else {
            // 失败会返回错误消息
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, resData.getStr("msg")));
            throw new ServiceException(resData.getStr("msg"));
        }

    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateTime.now());
        sysUserService.updateUserProfile(sysUser);
    }
}
