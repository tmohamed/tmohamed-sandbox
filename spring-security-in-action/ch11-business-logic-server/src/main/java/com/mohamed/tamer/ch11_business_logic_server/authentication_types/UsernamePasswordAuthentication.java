package com.mohamed.tamer.ch11_business_logic_server.authentication_types;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationToken {
    public UsernamePasswordAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities){
        super(principal, credentials, authorities);
    }

    public UsernamePasswordAuthentication(Object principal, Object credentials){
        super(principal, credentials);
    }
}
