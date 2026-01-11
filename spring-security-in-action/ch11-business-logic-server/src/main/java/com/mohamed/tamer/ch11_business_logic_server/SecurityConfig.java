package com.mohamed.tamer.ch11_business_logic_server;

import com.mohamed.tamer.ch11_business_logic_server.authentication_providers.OtpAuthenticationProvider;
import com.mohamed.tamer.ch11_business_logic_server.authentication_providers.UsernamePasswordAuthenticationProvider;
import com.mohamed.tamer.ch11_business_logic_server.filters.InitialAuthenticationFilter;
import com.mohamed.tamer.ch11_business_logic_server.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final InitialAuthenticationFilter initialAuthenticationFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;
    private final OtpAuthenticationProvider otpAuthenticationProvider;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable());

        http.authenticationManager(authenticationManager());

        http.addFilterAt(initialAuthenticationFilter, BasicAuthenticationFilter.class);
        http.addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class);

        http.authorizeHttpRequests(c -> c.anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(usernamePasswordAuthenticationProvider, otpAuthenticationProvider);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
