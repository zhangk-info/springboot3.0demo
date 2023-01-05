package com.xlj.framework.configuration.auth.oauth2.client.impl;//package com.xlj.configuration.auth.oauth2.client.impl;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OidcOAuth2Client {
//
//	@Value("${oauth2.token.uri}")
//   	private String oauth2TokenUri;
//	@Value("${oauth2.authorization.uri}")
//	private String oauth2AuthorizationUri;
//
//	public ClientRegistration getClientRegistration() {
//		return ClientRegistration.withRegistrationId("messaging-client-oidc")
//            .clientId("messaging-client")
//            .clientSecret("secret")
//            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//            .scope("openid")
//            .authorizationUri(oauth2AuthorizationUri)
//            .tokenUri(oauth2TokenUri)
//            .clientName("messaging-client-oidc")
//            .build();
//	}
//
//}
