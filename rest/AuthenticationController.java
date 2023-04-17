package com.go.tokenverification.rest;

import com.go.tokenverification.entity.UserEntity;
import com.go.tokenverification.exception.InvalidDataException;
import com.go.tokenverification.service.AuthenticationProviderService;
import com.go.tokenverification.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationProviderService authenticationProviderService;

    @PostMapping("/signUp")
    public void signUp(@RequestBody UserEntity user) throws InvalidDataException {
        userService.addUser(user);
    }

    @PostMapping("/login")
    public String login(){
        return "Login Successfully";
    }

}
