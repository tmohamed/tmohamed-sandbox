package com.mohamed.tamer.ch11_authentication_server.controller;

import com.mohamed.tamer.ch11_authentication_server.model.entity.Otp;
import com.mohamed.tamer.ch11_authentication_server.model.entity.User;
import com.mohamed.tamer.ch11_authentication_server.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/user/add")
    public void addUser(@RequestBody User user){
        userService.addUser(user);
    }

    @PostMapping("/user/auth")
    public void auth(@RequestBody User user){
        userService.auth(user);
    }

    @PostMapping("/otp/check")
    public void checkOtp(@RequestBody Otp otp, HttpServletResponse response){
        if(userService.check(otp)){
            response.setStatus(HttpServletResponse.SC_OK);
        } else{
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

}
