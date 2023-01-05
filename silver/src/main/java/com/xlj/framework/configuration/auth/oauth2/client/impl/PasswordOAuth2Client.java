package com.xlj.framework.configuration.auth.oauth2.client.impl;

import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

@Component
public class PasswordOAuth2Client extends AbstractOAuth2Client {

	public PasswordOAuth2Client(Environment env) {
		super(env);
	}
	
	@Override
	public ClientRegistration getClientRegistration() {
		return ClientRegistration.withRegistrationId("password")
           .clientId("xinglianjing")
           .clientSecret("xinglianjing")
			// client_secret_basic 是将 clientId 和 clientSecret 通过 ‘:’ 号拼接，并使用 Base64 进行编码得到一个字符串。将此编码字符串放到请求头(Authorization) 去发送请求；
			// 而 client_secret_post 是将 clientId 和 clientSecret 放到请求体(表单) 去发送请求
           .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
           .authorizationGrantType(AuthorizationGrantType.PASSWORD)
           //.scope("message.read", "message.write")
           .authorizationUri(oauth2AuthorizationUri)
           .tokenUri(oauth2TokenUri)			
           .build();
	}

}
