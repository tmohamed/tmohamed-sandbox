package com.tamer.spring_resource_server.config;

import com.tamer.spring_resource_server.custom_token.CustomAuthentication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, CustomAuthentication> {
    @Override
    public CustomAuthentication convert(Jwt source) {
        List<GrantedAuthority> authorities =
                List.of(() -> "read");

        String priority = String.valueOf(source.getClaims().get("priority"));

        return new CustomAuthentication(source, authorities, priority);
    }
}
