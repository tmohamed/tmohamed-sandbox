package com.mohamed.tamer.ch11_business_logic_server.authentication_providers;

import com.mohamed.tamer.ch11_business_logic_server.authentication_server_proxy.AuthenticationServerProxy;
import com.mohamed.tamer.ch11_business_logic_server.authentication_types.UsernamePasswordAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private final AuthenticationServerProxy authenticationServerProxy;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());
        this.authenticationServerProxy.sendAuth(username, password);

        return new UsernamePasswordAuthentication(username, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(authentication);
    }
}
