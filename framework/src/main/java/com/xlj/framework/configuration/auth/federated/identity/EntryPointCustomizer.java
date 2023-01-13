package com.xlj.framework.configuration.auth.federated.identity;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xlj.common.constants.CacheConstants;
import com.xlj.common.constants.Constants;
import com.xlj.common.entity.DataResp;
import com.xlj.system.configuration.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EntryPointCustomizer implements AuthenticationEntryPoint {

    @Autowired
    private RedisService redisCache;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        String message;
        if (exception instanceof UsernameNotFoundException) {
            message = "用户名或密码错误";
        } else if (exception instanceof LockedException) {
            message = "用户已锁定";
        } else if (exception instanceof DisabledException) {
            message = "账户不可用";
        }else if (exception instanceof BadCredentialsException) {
            message = "用户名或密码错误";
        }else {
            message = exception.getClass().getSimpleName() + "::" + exception.getMessage();
        }
        // 登录失败后账号失败次数+1
        String username = request.getParameter("username");
        if (JSONUtil.isTypeJSON(username)) {
            // 如果是json表示是后台用户登录，走后台登录逻辑
            JSONObject jsonObject = JSONUtil.parseObj(username);
            username = jsonObject.getStr("username");
        }
        Integer retryCount = (Integer) redisCache.get(getCacheKey(username));
        if (retryCount == null) {
            retryCount = 0;
        }
        // 记录登录次数
        retryCount = retryCount + 1;
        redisCache.set(getCacheKey(username), retryCount, Constants.LOGIN_LOCK_TIME);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(JSONUtil.toJsonStr(DataResp.error(message)));
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
}