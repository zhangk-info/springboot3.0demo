package com.xlj.framework.filter.web_security;

import com.xlj.common.constants.CacheConstants;
import com.xlj.common.properties.UriProperties;
import com.xlj.system.configuration.RedisService;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class BeforeTokenAuthorizationFilter extends OncePerRequestFilter {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final RedisService redisService;
    @Resource
    private final UriProperties urlProperties;

    public BeforeTokenAuthorizationFilter(RedisService redisService, UriProperties urlProperties) {
        this.redisService = redisService;
        this.urlProperties = urlProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // cors处理 对于OPTIONS请求直接返回成功 前端对于post请求 不管有没有跨域都会发options请求
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }
        // 需要跳过的直接跳过
        if (matcher(request.getRequestURI()) || antPathMatcher.match("/logout", request.getRequestURI())) {
            filterChain.doFilter(request, response);
        } else {
            HeaderMapRequestWrapper headerMapRequestWrapper = new HeaderMapRequestWrapper(request);
            String authToken = request.getHeader("Authorization");
            if (null == authToken) {
                authToken = request.getParameter("t");
            }
            if (null == authToken) {
                log.error("请使用正确的认证方式：" + request.getRequestURI());
                // 这里要向下流转，让authorizationFilter处理异常，自己抛出异常会被抓然后返回500错误而不是401。也不能自己抛出后return，因为这个只是链上的一个
                filterChain.doFilter(headerMapRequestWrapper, response);
                return;
            }
            String token = StringUtils.substringAfter(authToken, "Bearer ").trim();
            // 从缓存中转换缩短的token为正常的token
            if (Objects.isNull(redisService.get(CacheConstants.LOGIN_TOKEN_KEY + token))) {
                // 这里要向下流转，让authorizationFilter处理异常，自己抛出异常会被抓然后返回500错误而不是401。也不能自己抛出后return，因为这个只是链上的一个
                filterChain.doFilter(headerMapRequestWrapper, response);
                return;
            }
            String beforeToken = (String) redisService.get(CacheConstants.LOGIN_TOKEN_KEY + token);
            headerMapRequestWrapper.addHeader("Authorization", "Bearer " + beforeToken);
            filterChain.doFilter(headerMapRequestWrapper, response);
        }
    }

    /**
     * 匹配需要放开的路径
     *
     * @param requestUrl requestUrl
     * @return 是否匹配
     */
    private boolean matcher(String requestUrl) {
        for (String url : urlProperties.getIgnores()) {
            if (antPathMatcher.match(url, requestUrl)) {
                return true;
            }
        }
        for (String url : urlProperties.getPublicIgnores()) {
            if (antPathMatcher.match(url, requestUrl)) {
                return true;
            }
        }
        return false;
    }
}
