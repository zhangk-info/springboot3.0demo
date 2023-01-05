package com.xlj.framework.filter.captch;

import cn.hutool.core.util.StrUtil;
import com.xlj.system.service.SysLoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * 验证码验证filter
 *
 * @author zhangkun
 */
@Component
@Order(99)
public class ImageCodeValidateFilter extends OncePerRequestFilter {

    @Autowired
    private SysLoginService sysLoginService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals("/oauth2/token", request.getRequestURI()) &&
                StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            String code = request.getParameter("code");
            String signature = request.getParameter("signature");
            String username = request.getParameter("username");
            if (StrUtil.isNotEmpty(code)) {
                sysLoginService.validateCaptcha(username, code, signature);
            }
        }
        filterChain.doFilter(request, response);
    }

}
