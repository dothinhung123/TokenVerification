package com.go.tokenverification.jwt;

import java.time.ZonedDateTime;

public class JwtExpiredException extends Exception{


    private final String subject;
    private final ZonedDateTime expiredTime;

    public JwtExpiredException(String message, String subject, ZonedDateTime expiredTime) {
        super(message);
        this.subject = subject;
        this.expiredTime = expiredTime;
    }

}
