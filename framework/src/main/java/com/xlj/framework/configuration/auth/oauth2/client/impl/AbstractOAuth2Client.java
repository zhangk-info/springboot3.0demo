package com.xlj.framework.configuration.auth.oauth2.client.impl;

import com.xlj.framework.configuration.auth.oauth2.client.OAuth2Client;
import org.springframework.core.env.Environment;

public abstract class AbstractOAuth2Client implements OAuth2Client {

	protected String oauth2AuthorizationUri;
	protected String oauth2TokenUri;

	public AbstractOAuth2Client(Environment env) {
		this.oauth2AuthorizationUri = env.getProperty("oauth2.authorization.uri");
		this.oauth2TokenUri = env.getProperty("oauth2.token.uri");
	}

}
