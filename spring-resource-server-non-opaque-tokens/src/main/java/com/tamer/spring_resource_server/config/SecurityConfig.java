package com.tamer.spring_resource_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${keySetURI}")
    private String keySetUri;

    @Autowired
    private JwtAuthenticationConverter authenticationConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(
                c-> c.jwt(
                        j->j.jwkSetUri(keySetUri)
                                .jwtAuthenticationConverter(authenticationConverter)));

        http.authorizeHttpRequests(
                c -> c.anyRequest().authenticated());

        return http.build();
    }

}
