package com.xlj.framework.configuration.auth;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.xlj.common.properties.UriProperties;
import com.xlj.common.sgcc.Sm4Utils;
import com.xlj.framework.configuration.auth.authentication.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.xlj.framework.configuration.auth.authentication.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import com.xlj.framework.configuration.auth.converter.CustomJwtGrantedAuthoritiesConverter;
import com.xlj.framework.configuration.auth.customizer.jwt.JwtCustomizer;
import com.xlj.framework.configuration.auth.customizer.jwt.JwtCustomizerHandler;
import com.xlj.framework.configuration.auth.customizer.jwt.impl.JwtCustomizerImpl;
import com.xlj.framework.configuration.auth.customizer.token.claims.OAuth2TokenClaimsCustomizer;
import com.xlj.framework.configuration.auth.customizer.token.claims.impl.OAuth2TokenClaimsCustomizerImpl;
import com.xlj.framework.configuration.auth.federated.identity.EntryPointCustomizer;
import com.xlj.framework.configuration.auth.handler.CustomerAuthenticationFailureHandler;
import com.xlj.framework.configuration.auth.handler.CustomerAuthenticationSuccessHandler;
import com.xlj.framework.configuration.auth.handler.MyLogoutHandler;
import com.xlj.framework.configuration.auth.jose.Jwks;
import com.xlj.framework.configuration.password.SM4PasswordEncoder;
import com.xlj.framework.filter.context.CurrentUserFilter;
import com.xlj.framework.filter.web_security.BeforeTokenAuthorizationFilter;
import com.xlj.framework.filter.web_security.XssFilter;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

/**
 * authorization server配置主类
 *
 * @author zhangkun
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
public class AuthorizationServerConfiguration {

    private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

    @Value("${oauth2.token.issuer}")
    private String tokenIssuer;

    @Resource
    private MyLogoutHandler myLogoutHandler;
    @Autowired
    private CustomerAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private CustomerAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private Sm4Utils sm4Utils;
    @Autowired
    private EntryPointCustomizer entryPointCustomizer;
    @Resource
    private UriProperties urlProperties;
    @Autowired
    private XssFilter xssFilter;
    @Autowired
    @Lazy
    private CurrentUserFilter currentUserFilter;
    @Autowired
    private BeforeTokenAuthorizationFilter beforeTokenAuthorizationFilter;

    /**
     * PasswordEncoder
     * Auto wired passwordEncoder in AuthorizationServerConfig class;
     * for fix solve "There is no PasswordEncoder mapped for the id "null" " exception
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SM4PasswordEncoder(sm4Utils);
    }

    /**
     * 主要配置
     *
     * @param http http
     * @return SecurityFilterChain
     * @throws Exception e
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        // 配置OAuth2 Token endpoint
        authorizationServerConfigurer.tokenEndpoint((tokenEndpoint) -> {
            tokenEndpoint.accessTokenRequestConverter(
                    new DelegatingAuthenticationConverter(Arrays.asList(
                            new OAuth2RefreshTokenAuthenticationConverter(),
                            new OAuth2ResourceOwnerPasswordAuthenticationConverter()))
            );
            tokenEndpoint.accessTokenResponseHandler(authenticationSuccessHandler);
            tokenEndpoint.errorResponseHandler(authenticationFailureHandler);
        });

        // 配置OAuth2 Authorization endpoint.
        authorizationServerConfigurer.authorizationEndpoint(authorizationEndpoint -> {
        });

        http.apply(authorizationServerConfigurer);

        // 这个里面居然不包含/logout
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http
                .securityMatcher(endpointsMatcher)
                .securityMatcher("/logout")
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(entryPointCustomizer)
                );

        SecurityFilterChain securityFilterChain = http.formLogin(Customizer.withDefaults()).build();
        http.logout().logoutUrl("/logout").logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()).addLogoutHandler(myLogoutHandler);
        /*
         * Custom configuration for Resource Owner Password grant type. Current implementation has no support for Resource Owner
         * Password grant type
         */
        addCustomOAuth2ResourceOwnerPasswordAuthenticationProvider(http);

        return securityFilterChain;
    }

    /**
     * 配置ResourceServer以及退出登录
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChainForResourceServer(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/oauth2/token").permitAll()
                        .requestMatchers(urlProperties.getIgnores().toArray(new String[0])).permitAll()
                        .requestMatchers(urlProperties.getPublicIgnores().toArray(new String[0])).permitAll()
                        .anyRequest()
                        .authenticated())
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(entryPointCustomizer)
                );
        // spring security 5.x默认的bearer token解析器没有启用从请求参数中获取token 配置这个以启用filter
        http.oauth2ResourceServer().jwt(jwt -> jwt.decoder(jwtDecoder(jwkSource())).jwtAuthenticationConverter(jwtAuthenticationConverter())).bearerTokenResolver(new DefaultBearerTokenResolver());
        http.addFilterBefore(beforeTokenAuthorizationFilter, BearerTokenAuthenticationFilter.class);

        return http.build();
    }

    /**
     * JwtAuthenticationConverter
     *
     * @return JwtAuthenticationConverter
     */
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        CustomJwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new CustomJwtGrantedAuthoritiesConverter();
        // 默认前缀是SCOPE_ 这里去掉了
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(false);
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setMaxAge(Duration.ofHours(1));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 确认授权持久化
     *
     * @param jdbcTemplate               jdbcTemplate
     * @param registeredClientRepository registeredClientRepository
     * @return OAuth2AuthorizationConsentService
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * jwk认证配置
     *
     * @return JWKSource
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsaByStaticFile();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    /**
     * JwtDecoder
     *
     * @param jwkSource jwkSource
     * @return JwtDecoder
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * AuthorizationServerSettings配置，这里只需要配置issuer
     * setting issuer
     *
     * @return AuthorizationServerSettings
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().issuer(tokenIssuer).build();
    }

    /**
     * 自定义jwt内容
     *
     * @return OAuth2TokenCustomizer
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> buildJwtCustomizer() {

        JwtCustomizerHandler jwtCustomizerHandler = JwtCustomizerHandler.getJwtCustomizerHandler();
        JwtCustomizer jwtCustomizer = new JwtCustomizerImpl(jwtCustomizerHandler);

        return jwtCustomizer::customizeToken;
    }

    @Bean
    public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> buildOAuth2TokenClaimsCustomizer() {

        OAuth2TokenClaimsCustomizer oauth2TokenClaimsCustomizer = new OAuth2TokenClaimsCustomizerImpl();
        OAuth2TokenCustomizer<OAuth2TokenClaimsContext> customizer = (context) -> {
            oauth2TokenClaimsCustomizer.customizeTokenClaims(context);
        };

        return customizer;
    }

    /**
     * 自定义认证方式配置
     *
     * @param http http
     */
    private void addCustomOAuth2ResourceOwnerPasswordAuthenticationProvider(HttpSecurity http) {

        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = http.getSharedObject(OAuth2TokenGenerator.class);

        OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider =
                new OAuth2ResourceOwnerPasswordAuthenticationProvider(authenticationManager, authorizationService, tokenGenerator);

        // This will add new authentication provider in the list of existing authentication providers.
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);

    }

}
