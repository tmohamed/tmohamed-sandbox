package com.mohamed.tamer.ch11_business_logic_server.config;

import com.mohamed.tamer.ch11_business_logic_server.authentication_providers.OtpAuthenticationProvider;
import com.mohamed.tamer.ch11_business_logic_server.authentication_providers.UsernamePasswordAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

@Configuration
@RequiredArgsConstructor
public class AuthenticationManagerConfig {
    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;
    private final OtpAuthenticationProvider otpAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(usernamePasswordAuthenticationProvider, otpAuthenticationProvider);
    }
}
