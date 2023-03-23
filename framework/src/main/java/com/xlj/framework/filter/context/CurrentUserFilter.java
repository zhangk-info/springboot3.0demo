package com.xlj.framework.filter.context;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.xlj.common.constants.CacheConstants;
import com.xlj.common.context.CurrentUser;
import com.xlj.common.context.UserContext;
import com.xlj.common.context.UserType;
import com.xlj.common.entity.DataResp;
import com.xlj.common.exception.ServiceException;
import com.xlj.common.properties.UriProperties;
import com.xlj.framework.configuration.auth.common.SecurityUserDetails;
import com.xlj.system.configuration.RedisService;
import com.xlj.system.domain.model.LoginUser;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhangkun
 */
@Component
@Slf4j
@Order(100)
public class CurrentUserFilter extends OncePerRequestFilter {
    private static final String ALLOWED_HEADERS = "*";
    private static final String ALLOWED_METHODS = "*";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_EXPOSE = "*";
    private static final String MAX_AGE = "3600";
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Resource
    private UriProperties urlProperties;
    @Resource
    private JwtDecoder jwtDecoder;
    @Autowired
    private RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        // cors处理 对于OPTIONS请求直接返回成功 前端对于post请求 不管有没有跨域都会发options请求
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
            response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
            response.setHeader("Access-Control-Allow-Methods", ALLOWED_METHODS);
            response.setHeader("Access-Control-Max-Age", MAX_AGE);
            response.setHeader("Access-Control-Allow-Headers", ALLOWED_HEADERS);
            response.setHeader("Access-Control-Expose-Headers", ALLOWED_EXPOSE);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            return;
        }
        log.info(" -- " + request.getRequestURI());
        // 需要跳过的直接跳过
        if (matcher(request.getRequestURI()) || antPathMatcher.match("/logout", request.getRequestURI())) {
            filterChain.doFilter(request, response);
        } else {
            try {
                String authToken = request.getHeader("Authorization");
                if (null == authToken) {
                    authToken = request.getParameter("t");
                }
                if (null == authToken) {
                    throw new ServiceException("请使用正确的认证方式");
                }
                String token = StringUtils.substringAfter(authToken, "Bearer ").trim();

                if (Objects.isNull(redisService.get(CacheConstants.LOGIN_TOKEN_KEY + token))) {
                    throw new ServletException("认证过期");
                }
                String beforeToken = (String) redisService.get(CacheConstants.LOGIN_TOKEN_KEY + token);

                Jwt jwt = jwtDecoder.decode(beforeToken);
                // 从jwt中解析出用户信息
                Map<String, Object> claims = jwt.getClaims();
                UserDetails userDetails;
                // 如果包含user字段，那么是LoginUser
                if (claims.containsKey("user")) {
                    userDetails = JSONUtil.toBean(JSONUtil.toJsonStr(claims), LoginUser.class, true);
                } else {
                    userDetails = JSONUtil.toBean(JSONUtil.toJsonStr(claims), SecurityUserDetails.class, true);
                }
                // 获取用户共有且需要的信息转换成CurrentUser并放入ThreadLocal
                CurrentUser currentUser = new CurrentUser();
                BeanUtil.copyProperties(userDetails, currentUser);
                if (claims.containsKey("user")) {
                    currentUser.setSysUser(true);
                }
                UserContext.set(currentUser);
                log.debug("current currentUser is : {}", currentUser);
                // 重新设置权限
                setAuthorizationWithAuthority(request, userDetails);
                log.info("set authority success : {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            } catch (Exception e) {
                log.error("current user filter error. url is [{}]", request.getRequestURI(), e);
                response.setStatus(401);
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                if (e instanceof BadJwtException) {
                    response.getWriter().print(JSONUtil.toJsonStr(DataResp.error("认证过期，请重新登录")));
                } else {
                    response.getWriter().print(JSONUtil.toJsonStr(DataResp.error(e.getMessage())));
                }
                return;
            }
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 重新设置SecurityContext的Authentication，用于重新设置roles
     *
     * @param request request
     */
    private void setAuthorizationWithAuthority(HttpServletRequest request, UserDetails userDetails) {

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();

        // 根据用户类型设置不同的权限
        Set<String> authorities;
        if (userDetails instanceof LoginUser loginUser) {
            authorities = loginUser.getPermissions();
            // 后台的设置到token中
            for (String role : authorities) {
                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                simpleGrantedAuthorities.add(grantedAuthority);
            }
            // 给权限加入UserType.SYSTEM
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(UserType.SYSTEM.name()));
        } else {
            // TODO 得到当前用户的所有权限
            SecurityUserDetails securityUserDetails = (SecurityUserDetails) userDetails;
            List<String> roles = new ArrayList<>();
            for (String role : roles) {
                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                simpleGrantedAuthorities.add(grantedAuthority);
            }
            // 给权限加入UserType.DEFAULT
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(UserType.DEFAULT.name()));
        }
        // 生成新的AuthenticationToken
        PreAuthenticatedAuthenticationToken auth = new PreAuthenticatedAuthenticationToken(userDetails, null, simpleGrantedAuthorities);
        auth.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
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

