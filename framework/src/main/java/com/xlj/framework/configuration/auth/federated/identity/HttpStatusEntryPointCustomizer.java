package com.xlj.framework.configuration.auth.federated.identity;

import cn.hutool.json.JSONUtil;
import com.xlj.common.entity.DataResp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class HttpStatusEntryPointCustomizer implements AuthenticationEntryPoint {
    private final HttpStatus httpStatus;

    public HttpStatusEntryPointCustomizer(@NotNull HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        response.setStatus(this.httpStatus.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        try {
            response.getWriter().print(JSONUtil.toJsonStr(DataResp.error(authException.getMessage())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}