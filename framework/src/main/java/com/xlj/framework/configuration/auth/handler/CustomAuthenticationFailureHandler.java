package com.xlj.framework.configuration.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xlj.common.constants.CacheConstants;
import com.xlj.common.constants.Constants;
import com.xlj.common.utils.MessageUtils;
import com.xlj.framework.manager.AsyncManager;
import com.xlj.framework.manager.factory.AsyncFactory;
import com.xlj.system.configuration.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义AuthenticationException返回的信息
 *
 * @author zhangkun
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisService redisCache;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> data = new HashMap<>(2);
        data.put("timestamp", Calendar.getInstance().getTime());
        data.put("exception", exception.getMessage());

        String message;
        if (exception instanceof UsernameNotFoundException) {
            message = "用户名或密码错误";
        } else if (exception instanceof LockedException) {
            message = "用户已锁定";
        } else if (exception instanceof DisabledException) {
            message = "账户不可用";
        } else if (exception instanceof BadCredentialsException) {
            message = "用户名或者密码错误";
        } else {
            message = exception.getClass().getSimpleName() + "::" + exception.getMessage();
        }

        // 登录失败后账号失败次数+1
        String username = request.getParameter("username");
        Integer retryCount = (Integer) redisCache.get(getCacheKey(username));
        if (retryCount == null) {
            retryCount = 0;
        }
        // 记录登录次数
        retryCount = retryCount + 1;
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                MessageUtils.message("user.password.retry.limit.count", retryCount)));
        redisCache.set(getCacheKey(username), retryCount, Constants.LOGIN_LOCK_TIME);

        // 输出
        response.getOutputStream()
                .println(objectMapper.writeValueAsString(data));
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