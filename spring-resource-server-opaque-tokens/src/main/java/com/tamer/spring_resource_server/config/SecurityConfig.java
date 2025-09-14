package com.tamer.spring_resource_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${introspection-uri}")
    private String introspectionUri;

    @Value("${resrouce-server-client-id}")
    private String resourceServerClientId;

    @Value("${resrouce-server-client-secret}")
    private String resourceServerClientSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(
                c-> c.opaqueToken(
                        o-> o.introspectionUri(introspectionUri)
                                .introspectionClientCredentials(resourceServerClientId, resourceServerClientSecret)
                )
        );

        http.authorizeHttpRequests(
                c -> c.anyRequest().authenticated());

        return http.build();
    }

}
