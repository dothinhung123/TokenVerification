package com.go.tokenverification.exception;

public class EmailConfirmationTokenNotFoundException extends Exception{
    private String token;

    public EmailConfirmationTokenNotFoundException(String message, String token){
        super(message);
        this.token = token;
    }
}
