package com.mohamed.tamer.ch11_authentication_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class Ch11AuthenticationServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(Ch11AuthenticationServerApplication.class, args);
    }
}