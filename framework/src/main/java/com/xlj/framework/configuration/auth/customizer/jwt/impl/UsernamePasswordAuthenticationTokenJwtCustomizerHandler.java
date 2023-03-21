package com.xlj.framework.configuration.auth.customizer.jwt.impl;

import cn.hutool.core.bean.BeanUtil;
import com.xlj.framework.configuration.auth.common.SecurityUserDetails;
import com.xlj.framework.configuration.auth.customizer.jwt.JwtCustomizerHandler;
import com.xlj.system.domain.model.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义jwt内容
 *
 * @author zhangkun
 */
public class UsernamePasswordAuthenticationTokenJwtCustomizerHandler extends AbstractJwtCustomizerHandler {

    public UsernamePasswordAuthenticationTokenJwtCustomizerHandler(JwtCustomizerHandler jwtCustomizerHandler) {
        super(jwtCustomizerHandler);
    }

    /**
     * 自定义jwt内容
     *
     * @param jwtEncodingContext
     */
    @Override
    protected void customizeJwt(JwtEncodingContext jwtEncodingContext) {
        Map<String, Object> userAttributes = new HashMap<>();

        Authentication authentication = jwtEncodingContext.getPrincipal();

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Set<String> authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        if (userPrincipal instanceof LoginUser loginUser) {
            userAttributes.putAll(BeanUtil.beanToMap(loginUser));
        } else {
            SecurityUserDetails securityUserDetails = (SecurityUserDetails) userPrincipal;
            userAttributes.putAll(BeanUtil.beanToMap(securityUserDetails));
        }

        Set<String> contextAuthorizedScopes = jwtEncodingContext.getAuthorizedScopes();

        JwtClaimsSet.Builder jwtClaimSetBuilder = jwtEncodingContext.getClaims();

        if (CollectionUtils.isEmpty(contextAuthorizedScopes)) {
            jwtClaimSetBuilder.claim(OAuth2ParameterNames.SCOPE, authorities);
        }

        jwtClaimSetBuilder.claims(claims ->
                userAttributes.entrySet()
                        .forEach(entry -> claims.put(entry.getKey(), entry.getValue()))
        );

    }

    @Override
    protected boolean supportCustomizeContext(Authentication authentication) {
        return authentication != null && authentication instanceof UsernamePasswordAuthenticationToken;
    }

}
