package com.mohamed.tamer.ch11_business_logic_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class Ch11BusinessLogicServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(Ch11BusinessLogicServerApplication.class, args);
    }
}