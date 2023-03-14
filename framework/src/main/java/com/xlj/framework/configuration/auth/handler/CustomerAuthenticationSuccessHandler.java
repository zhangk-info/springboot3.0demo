package com.xlj.framework.configuration.auth.handler;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xlj.common.constants.CacheConstants;
import com.xlj.common.spring.SpringUtils;
import com.xlj.system.configuration.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * 自定义登录成功后处理
 */
@Component
public class CustomerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RedisService redisService;

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
        String username = request.getParameter("username");
        if (JSONUtil.isTypeJSON(username)) {
            // 如果是json表示是后台用户登录，走后台登录逻辑
            JSONObject jsonObject = JSONUtil.parseObj(username);
            username = jsonObject.getStr("username");
        }
        if (redisService.hasKey(getCacheKey(username))) {
            redisService.del(getCacheKey(username));
        }

        // OAuth2TokenEndpointFilter.authenticationSuccessHandler
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
                (OAuth2AccessTokenAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        // 转换token 并保存到redis
        String beforeToken = accessToken.getTokenValue();
        String newToken = IdUtil.fastSimpleUUID();
        redisService.set(CacheConstants.LOGIN_TOKEN_KEY + newToken, beforeToken, 86400 - 100);
        Map<String, Object> claims = ((JwtDecoder) SpringUtils.getBean("jwtDecoder")).decode(beforeToken).getClaims();
        // 如果包含user字段，那么是LoginUser
        Long userId = (Long) claims.get("userId");
        if (claims.containsKey("user")) {
            // 后台用户不用保存用户已登录token，因为不用强制退出
            redisService.set(CacheConstants.USER_TOKEN + "sys:" + userId + ":" + newToken, beforeToken, 86400 - 100);
        } else {
            // 普通用户保存用户已登录token，用于强制退出
            redisService.set(CacheConstants.USER_TOKEN + userId + ":" + newToken, beforeToken, 86400 - 100);
        }

        OAuth2AccessTokenResponse.Builder builder =
                OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                        .tokenType(accessToken.getTokenType())
                        .scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        new OAuth2AccessTokenResponseHttpMessageConverter().write(accessTokenResponse, null, httpResponse);

    }
}
