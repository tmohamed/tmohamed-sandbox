package com.mohamed.tamer.ch5_filters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class Ch5FiltersApplication {
    public static void main(String[] args) {
        SpringApplication.run(Ch5FiltersApplication.class, args);
    }
}