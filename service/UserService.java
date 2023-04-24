package com.go.tokenverification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.go.tokenverification.entity.EmailConfirmationTokenEntity;
import com.go.tokenverification.entity.UserEntity;
import com.go.tokenverification.exception.InvalidDataException;
import com.go.tokenverification.jwt.JwtTokenService;
import com.go.tokenverification.model.security.User;
import com.go.tokenverification.repository.EmailConfirmationTokenRepository;
import com.go.tokenverification.repository.UserRepository;
import com.go.tokenverification.utils.EmailValidationUtils;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtTokenService jwtTokenService;

    @Value("${email.token.expire.minutes}")
    private String emailTokenExpire;


    public UserService(UserRepository userRepository, EmailService emailService, EmailConfirmationTokenRepository emailConfirmationTokenRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = findActiveUserByUsername(username);
        log.info("FOUND, User with username =  {}", username);
        return new User(user);
    }

    public void addUser(UserEntity user) throws InvalidDataException, JOSEException, JsonProcessingException {
        //verify email
        if(user==null || Strings.isEmpty(user.getUsername()) || Strings.isEmpty(user.getPassword())
        || !EmailValidationUtils.validateEmail(user.getUsername())){
            log.error("User information is not valid {}", new ObjectMapper().writeValueAsString(user));
            throw new InvalidDataException("User is invalid");
        }

        if(isUserExist(user)){
            log.error("User with username = {} have already exist", user.getUsername());
            throw new InvalidDataException("User have already existed");
        }

        log.info("START add addUser with username = {}", user.getUsername());
        //save user
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        //send email
        sendEmail(user);
        log.info("END add successfully addUser with username = {}", user.getUsername());
    }

    public void sendEmail(UserEntity user) throws JOSEException {
        log.info("START, sendEmail with username = {} ", user.getUsername());
        //create token
        String token = jwtTokenService.createToken(user.toString(),Long.parseLong(emailTokenExpire));

        //prepare email script
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getUsername());
        simpleMailMessage.setSubject("Complete Registration!");
        simpleMailMessage.setText("To confirm account, please click here : "
                +"http://localhost:8085/email/verification?token="+ token);

        EmailConfirmationTokenEntity emailConfirmationToken = new EmailConfirmationTokenEntity()
                .setToken(token)
                .setUser(user);

        //send email
        emailService.sendEmail(simpleMailMessage);

        //save email
        emailService.save(emailConfirmationToken);
        log.info("END, sendEmail with username = {} ", user.getUsername());
    }

    public boolean isUserExist(UserEntity user) throws InvalidDataException {
        if(user!= null && Strings.isEmpty(user.getUsername())){
            log.error("Username is invalid");
            throw new InvalidDataException("Username is empty or null");
        }
        return userRepository.findUserEntityByUsername(user.getUsername())
                .stream().findFirst().isPresent();

    }

    public UserEntity findActiveUserByUsername(String username){
        UserEntity user = userRepository.findActiveUserByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not exist in the system"));
        return user;
    }
    public UserEntity findInActiveUserByUsername(String username){
        UserEntity user = userRepository.findInactiveUserByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not exist in the system"));
        return user;
    }


}
