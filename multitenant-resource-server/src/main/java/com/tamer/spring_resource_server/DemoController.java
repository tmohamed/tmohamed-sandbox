package com.tamer.spring_resource_server;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public Authentication demo(Authentication authentication) {
        return authentication;
    }

}
