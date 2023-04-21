package com.go.tokenverification.service;

import com.go.tokenverification.entity.EmailConfirmationTokenEntity;
import com.go.tokenverification.entity.UserEntity;
import com.go.tokenverification.exception.EmailConfirmationTokenNotFoundException;
import com.go.tokenverification.jwt.JwtExpiredException;
import com.go.tokenverification.jwt.JwtTokenService;
import com.go.tokenverification.repository.EmailConfirmationTokenRepository;
import com.go.tokenverification.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    private final UserRepository userRepository;

    private final JwtTokenService jwtTokenService;

    @Value("${email.token.expire.minutes}")
    private String emailTokenExpireMinutes;
    public EmailService(JavaMailSender javaMailSender, EmailConfirmationTokenRepository emailConfirmationTokenRepository,UserRepository userRepository, JwtTokenService jwtTokenService) {
        this.javaMailSender = javaMailSender;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @Async
    public void sendEmail(SimpleMailMessage email){
        javaMailSender.send(email);
    }

    public void verifyEmailConfirmation(String token) throws EmailConfirmationTokenNotFoundException, ParseException, JOSEException, JwtExpiredException {
        //find token
        EmailConfirmationTokenEntity emailConfirmationTokenEntity = emailConfirmationTokenRepository.findByToken(token)
                .orElseThrow(()-> new EmailConfirmationTokenNotFoundException("Token is not existed", token));

        //check expire time
        JWTClaimsSet claimsSet = jwtTokenService.getJwtClaimsSet(token);
        if(jwtTokenService.isTokenExpire(claimsSet,Long.parseLong(emailTokenExpireMinutes))){
           throw new JwtExpiredException("Token is expired", claimsSet.getSubject());
        }

        // active user
        UserEntity user = userRepository.findInactiveUserByUsername(emailConfirmationTokenEntity.getUser().getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        user.setActive(Boolean.TRUE);

        userRepository.save(user);

    }
    public void save(EmailConfirmationTokenEntity emailConfirmationToken){
        emailConfirmationTokenRepository.save(emailConfirmationToken);
    }



}
