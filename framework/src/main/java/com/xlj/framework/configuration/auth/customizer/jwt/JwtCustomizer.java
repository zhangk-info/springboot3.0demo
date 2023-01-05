package com.xlj.framework.configuration.auth.customizer.jwt;

import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

public interface JwtCustomizer {

	void customizeToken(JwtEncodingContext context);
	
}
