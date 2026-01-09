package com.mohamed.tamer.ch11_business_logic_server.authentication_server_proxy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String username;
    private String password;
    private String code;
}
