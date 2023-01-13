package com.xlj.framework.configuration.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xlj.system.configuration.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *
 * 这里只能处理 OAuth2AuthorizationCodeRequestAuthenticationProvider 抛出的 OAuth2AuthorizationCodeRequestAuthenticationException
 * 其他错误在自定义的 authenticationEntryPoint 处理 .exceptionHandling((exceptions) -> exceptions.authenticationEntryPoint())
 * The AuthenticationFailureHandler (post-processor) used for handling an OAuth2AuthorizationCodeRequestAuthenticationException and returning the OAuth2Error response.
 *
 * @author zhangkun
 */
@Component
public class CustomerAuthenticationFailureHandler implements AuthenticationFailureHandler {

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
        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
        new OAuth2ErrorHttpMessageConverter().write(error, null, httpResponse);
    }
}