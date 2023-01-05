package com.xlj.framework.configuration.auth.customizer.jwt;

import com.xlj.framework.configuration.auth.customizer.jwt.impl.DefaultJwtCustomizerHandler;
import com.xlj.framework.configuration.auth.customizer.jwt.impl.OAuth2AuthenticationTokenJwtCustomizerHandler;
import com.xlj.framework.configuration.auth.customizer.jwt.impl.UsernamePasswordAuthenticationTokenJwtCustomizerHandler;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

public interface JwtCustomizerHandler {

    static JwtCustomizerHandler getJwtCustomizerHandler() {

        JwtCustomizerHandler defaultJwtCustomizerHandler = new DefaultJwtCustomizerHandler();
        JwtCustomizerHandler oauth2AuthenticationTokenJwtCustomizerHandler = new OAuth2AuthenticationTokenJwtCustomizerHandler(defaultJwtCustomizerHandler);
        JwtCustomizerHandler usernamePasswordAuthenticationTokenJwtCustomizerHandler = new UsernamePasswordAuthenticationTokenJwtCustomizerHandler(oauth2AuthenticationTokenJwtCustomizerHandler);
        return usernamePasswordAuthenticationTokenJwtCustomizerHandler;
    }

    void customize(JwtEncodingContext jwtEncodingContext);

}
