package com.go.tokenverification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class TokenVerificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TokenVerificationApplication.class, args);
    }

}
