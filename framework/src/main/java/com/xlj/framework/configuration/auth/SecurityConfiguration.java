package com.xlj.framework.configuration.auth;

import com.xlj.common.properties.UriProperties;
import com.xlj.framework.configuration.auth.converter.CustomJwtGrantedAuthoritiesConverter;
import com.xlj.framework.configuration.auth.federated.identity.FederatedIdentityConfigurer;
import com.xlj.framework.configuration.auth.federated.identity.UserRepositoryOAuth2UserHandler;
import com.xlj.framework.configuration.auth.service.SecurityUserDetailService;
import jakarta.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * spring security配置类
 * <p>
 * EnableMethodSecurity 启用鉴权
 * EnableWebSecurity 启用认证
 * * 自定义权限认证注解，使用方式：
 * {@link PermissionCheckService}
 * * @PreAuthorize("@pm.check('test')")
 * * security权限认证，使用方式：
 * * @PreAuthorize("hasAuthority('USER') or hasAuthority('message.read') ")
 * </p>
 *
 * @author zhangkun
 */
@EnableMethodSecurity(
        prePostEnabled = true,
        mode = AdviceMode.PROXY,
        proxyTargetClass = false
)
@EnableWebSecurity
public class SecurityConfiguration {

    private static final Logger LOGGER = LogManager.getLogger(SecurityConfiguration.class);

    @Autowired
    private SecurityUserDetailService userPrincipalService;
    /**
     * 认证服务在其他地方时需要（比如网关会需要这个）
     */
    @Value("${jwk.set.uri}")
    private String jwkSetUri;
    @Resource
    private UriProperties urlProperties;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 配置
     *
     * @param builder AuthenticationManager
     * @throws Exception
     */
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        LOGGER.debug("in configureGlobal");
        builder
                .userDetailsService(this.userPrincipalService)
                .passwordEncoder(passwordEncoder)
                .and()
                // 擦除凭证信息 将 Token 中的 credentials 属性置空
                .eraseCredentials(true)
                .build();
    }


    /**
     * 放开认证的路径
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                // 放开自定义的路径
                .requestMatchers(urlProperties.getIgnores().toArray(new String[0]))
                .requestMatchers(urlProperties.getPublicIgnores().toArray(new String[0]))
                .requestMatchers("/webjars/**", "/image/**");
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        LOGGER.debug("in configure HttpSecurity");

        FederatedIdentityConfigurer federatedIdentityConfigurer = new FederatedIdentityConfigurer().oauth2UserHandler(new UserRepositoryOAuth2UserHandler());

        http.authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests/*.requestMatchers(EndpointRequest.toAnyEndpoint(), PathRequest.toH2Console())
                                .permitAll()*/
                                .anyRequest()
                                .authenticated()
                )
//                .formLogin(form -> form.loginPage("/login").failureUrl("/login-error").permitAll())
                .csrf()/*.ignoringRequestMatchers(PathRequest.toH2Console())*/
                .and().headers().frameOptions().sameOrigin()
                .and()
                .apply(federatedIdentityConfigurer)
                .and()
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder)/*.decoder(jwtDecoder())*/.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    /**
     * JwtAuthenticationConverter
     *
     * @return
     */
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        CustomJwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new CustomJwtGrantedAuthoritiesConverter();
        // 默认前缀是SCOPE_ 这里去掉了
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
//    /**
//     * 认证服务在其他地方时需要（比如网关会需要这个）
//     *
//     * @return
//     */
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
//    }

}
