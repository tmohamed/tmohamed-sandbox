package com.tamer.spring_resource_server.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${tenant1.jwks_uri}")
    private String jwkSetUri;

    @Value("${tenant2.introspection-uri}")
    private String introspectionUri;

    @Value("${tenant2.resrouce-server-client-id}")
    private String resourceServerClientId;

    @Value("${tenant2.resrouce-server-client-secret}")
    private String resourceServerClientSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(
                j -> j.authenticationManagerResolver(authenticationManagerResolver(jwtDecoder(), opaqueTokenIntrospector())));

        http.authorizeHttpRequests(
                c -> c.anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector(){
        return new SpringOpaqueTokenIntrospector(introspectionUri, resourceServerClientId, resourceServerClientSecret);
    }

    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver(JwtDecoder jwtDecoder, OpaqueTokenIntrospector opaqueTokenIntrospector){
        AuthenticationManager jwtAuth = new ProviderManager(
                new JwtAuthenticationProvider(jwtDecoder)
        );

        AuthenticationManager opaqueAuth = new ProviderManager(
                new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector)
        );

        return (request) -> {
            if("tenant1".equals(request.getHeader("X-TenantID"))){
                return jwtAuth;
            } else {
                return opaqueAuth;
            }
        };
    }

}
