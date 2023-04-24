package com.go.tokenverification.service;

import com.go.tokenverification.model.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService implements AuthenticationProvider {

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SCryptPasswordEncoder sCryptPasswordEncoder;

    public AuthenticationService(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, SCryptPasswordEncoder sCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sCryptPasswordEncoder = sCryptPasswordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.info("Authenticate with username = {} ", username);
        User user = userService.loadUserByUsername(username);
        switch (user.getUser().getEncryptionAlgorithm()){
            case BCRYPT:
                return checkPassword(user,password, bCryptPasswordEncoder);

            case SCRYPT:
                return checkPassword(user, password, sCryptPasswordEncoder);

        }
        throw new BadCredentialsException("Bad credentials ");
    }

    private Authentication checkPassword(User user , String rawPassword , PasswordEncoder passwordEncoder){
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            log.info("password is correct");
            return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        } else {
            log.error("Password is incorrect");
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
