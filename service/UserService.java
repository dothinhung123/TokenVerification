package com.go.tokenverification.service;

import com.go.tokenverification.configure.CustomUserDetails;
import com.go.tokenverification.entity.EmailConfirmationTokenEntity;
import com.go.tokenverification.entity.UserEntity;
import com.go.tokenverification.exception.InvalidDataException;
import com.go.tokenverification.model.response.AcknowledgeResponse;
import com.go.tokenverification.repository.UserRepository;
import com.go.tokenverification.utils.EmailValidationUtils;
import net.bytebuddy.utility.RandomString;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${mail.expire.hours}")
    private String hours;


    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserEntityByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username is not existing in the system"));

        return new CustomUserDetails(user);
    }

    public void addUser(UserEntity user) throws InvalidDataException {
        //verify email
        if(user==null || Strings.isEmpty(user.getUsername()) || Strings.isEmpty(user.getPassword())
        || !EmailValidationUtils.validateEmail(user.getUsername())){
            throw new InvalidDataException("User is invalid");
        }

        if(isUserExist(user)){
            throw new InvalidDataException("User have already existed");
        }
        //send email
        String token = RandomString.make(64);
        EmailConfirmationTokenEntity emailConfirmationToken = sendEmail(user, token);

        //save
        user.setEmailConfirmationTokenEntity(emailConfirmationToken);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    private EmailConfirmationTokenEntity sendEmail(UserEntity user, String token) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getUsername());
        simpleMailMessage.setSubject("Complete Registration!");
        simpleMailMessage.setText("To confirm account, please click here : "
        +"http://localhost:8085/confirm-account?token="+ token);

        EmailConfirmationTokenEntity emailConfirmationToken = new EmailConfirmationTokenEntity()
                .setEmailConfirmationToken(token)
                .setEmailConfirmationTokenGeneratedAt(LocalDateTime.now())
                .setEmailConfirmationExpire(LocalDateTime.now().plusHours(Long.parseLong(hours)));
        emailService.sendEmail(simpleMailMessage);
        return emailConfirmationToken;
    }

    public boolean isUserExist(UserEntity user) throws InvalidDataException {
        if(user!= null && Strings.isEmpty(user.getUsername())){
            throw new InvalidDataException("Username is empty or null");
        }
        return userRepository.findUserEntityByUsername(user.getUsername())
                .stream().findFirst().isPresent();

    }



}
