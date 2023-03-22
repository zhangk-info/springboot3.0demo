package com.xlj.framework.filter.web_security;

import cn.hutool.json.JSONUtil;
import com.xlj.common.constants.CacheConstants;
import com.xlj.common.entity.DataResp;
import com.xlj.system.configuration.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class BeforeTokenAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisService redisService;

    private void responseError(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(401);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        if (StringUtils.isBlank(msg)) {
            response.getWriter().print(JSONUtil.toJsonStr(DataResp.error("认证过期，请重新登录")));
        } else {
            response.getWriter().print(JSONUtil.toJsonStr(DataResp.error(msg)));
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // cors处理 对于OPTIONS请求直接返回成功 前端对于post请求 不管有没有跨域都会发options请求
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }
        if (request.getRequestURI().equals("/oauth2/token") || request.getRequestURI().equals("/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        HeaderMapRequestWrapper headerMapRequestWrapper = new HeaderMapRequestWrapper(request);
        String authToken = request.getHeader("Authorization");
        if (null == authToken) {
            authToken = request.getParameter("t");
        }
        if (null == authToken) {
            // 这里要向下流转，让authorizationFilter处理异常，自己抛出异常会被抓然后返回500错误而不是401，除非自己render
            responseError(response, "请使用正确的认证方式");
            return;
        }
        String token = StringUtils.substringAfter(authToken, "Bearer ").trim();
        // 从缓存中转换缩短的token为正常的token
        if (Objects.isNull(redisService.get(CacheConstants.LOGIN_TOKEN_KEY + token))) {
            // 这里要向下流转，让authorizationFilter处理异常，自己抛出异常会被抓然后返回500错误而不是401，除非自己render
            filterChain.doFilter(headerMapRequestWrapper, response);
            return;
        }
        String beforeToken = (String) redisService.get(CacheConstants.LOGIN_TOKEN_KEY + token);
        headerMapRequestWrapper.addHeader("Authorization", "Bearer " + beforeToken);
        filterChain.doFilter(headerMapRequestWrapper, response);
    }
}
