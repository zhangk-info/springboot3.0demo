package com.xlj.framework.configuration.auth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
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

        response.getOutputStream()
                .println(objectMapper.writeValueAsString(data));
    }
}