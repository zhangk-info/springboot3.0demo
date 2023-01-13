package com.xlj.framework.configuration.auth.handler;

import com.xlj.common.constants.CacheConstants;
import com.xlj.system.configuration.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义登录成功后处理
 */
@Component
public class CustomerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RedisService redisCache;

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 登录成功后产出账户密码错误次数限制
        String loginName = request.getParameter("username");
        if (redisCache.hasKey(getCacheKey(loginName))) {
            redisCache.del(getCacheKey(loginName));
        }
    }
}
