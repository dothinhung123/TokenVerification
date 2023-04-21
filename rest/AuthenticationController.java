package com.go.tokenverification.rest;

import com.go.tokenverification.entity.UserEntity;
import com.go.tokenverification.exception.EmailConfirmationTokenNotFoundException;
import com.go.tokenverification.exception.InvalidDataException;
import com.go.tokenverification.jwt.JwtExpiredException;
import com.go.tokenverification.service.EmailService;
import com.go.tokenverification.service.UserService;
import com.nimbusds.jose.JOSEException;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class AuthenticationController {

    private final UserService userService;

    private final EmailService emailService;

    public AuthenticationController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody UserEntity user) throws InvalidDataException, JOSEException {
        userService.addUser(user);
    }

    @PostMapping("/login")
    public String login(){
        return "Login Successfully";
    }

    @GetMapping("/email/verification")
    public void String(@RequestParam String token) throws EmailConfirmationTokenNotFoundException, JwtExpiredException, ParseException, JOSEException {
         emailService.verifyEmailConfirmation(token);
    }

    @GetMapping("/send/email")
    public void sendEmail(@RequestParam("email") String email) throws JOSEException {
        UserEntity user = userService.findInActiveUserByUsername(email);
        userService.sendEmail(user);
    }

}
