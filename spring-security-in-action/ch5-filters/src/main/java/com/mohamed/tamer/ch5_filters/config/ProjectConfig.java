package com.mohamed.tamer.ch5_filters.config;

import com.mohamed.tamer.ch5_filters.filters.ApiKeyAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class ProjectConfig {

    @Autowired
    private ApiKeyAuthenticationFilter apiKeyAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.addFilterBefore(apiKeyAuthenticationFilter, BasicAuthenticationFilter.class);

        http.authorizeHttpRequests(c -> c.anyRequest().permitAll());

        return http.build();
    }
}
