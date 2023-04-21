package com.go.tokenverification.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        int strength = 10; // work factor of bcrypt
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(strength, new SecureRandom());
        return bCryptPasswordEncoder;
    }

    @Bean
    public SCryptPasswordEncoder sCryptPasswordEncoder() {
        int cpuCost = (int) Math.pow(2, 14); // factor to increase CPU costs
        int memoryCost = 8;      // increases memory usage
        int parallelization = 1; // currently not supported by Spring Security
        int keyLength = 32;      // key length in bytes
        int saltLength = 64;     // salt length in bytes

        SCryptPasswordEncoder sCryptPasswordEncoder = new SCryptPasswordEncoder(
                cpuCost,
                memoryCost,
                parallelization,
                keyLength,
                saltLength);
        return sCryptPasswordEncoder;
    }
}
