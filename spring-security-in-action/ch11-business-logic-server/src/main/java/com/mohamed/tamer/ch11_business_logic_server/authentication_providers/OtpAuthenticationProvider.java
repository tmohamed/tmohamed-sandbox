package com.mohamed.tamer.ch11_business_logic_server.authentication_providers;

import com.mohamed.tamer.ch11_business_logic_server.authentication_server_proxy.AuthenticationServerProxy;
import com.mohamed.tamer.ch11_business_logic_server.authentication_types.OtpAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OtpAuthenticationProvider implements AuthenticationProvider {
    private final AuthenticationServerProxy authenticationServerProxy;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String code = String.valueOf(authentication.getCredentials());
        boolean result = this.authenticationServerProxy.sendOtp(username, code);

        if(result){
            return new OtpAuthentication(username, code);
        }

        throw new BadCredentialsException("Bad credentials.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.isAssignableFrom(authentication);
    }
}
