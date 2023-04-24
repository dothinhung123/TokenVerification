package com.go.tokenverification.exception;

import com.go.tokenverification.jwt.JwtExpiredException;
import com.go.tokenverification.model.ErrorMessage;
import com.go.tokenverification.model.response.AcknowledgeResponse;
import com.nimbusds.jose.JOSEException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;

@ControllerAdvice
public class TokenVerificationExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleExternalException(Exception e, WebRequest request){
        String errorDescription = e.getLocalizedMessage();
        if(Strings.isEmpty(errorDescription)){
            errorDescription = e.toString();
        }

        ErrorMessage errorMessage = new ErrorMessage()
                .setErrorMessageDescription(errorDescription)
                .setOffsetDateTime(OffsetDateTime.now());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {InvalidDataException.class})
    public ResponseEntity<Object> handleInvalidDataException(InvalidDataException invalidDataException, WebRequest webRequest){
        String errorDescription = invalidDataException.getLocalizedMessage();
        if(Strings.isEmpty(errorDescription)){
            errorDescription = errorDescription.toString();
        }

        AcknowledgeResponse acknowledgeResponse = new AcknowledgeResponse()
                .setAcknowledge(Boolean.FALSE)
                .setOffsetDateTime(OffsetDateTime.now())
                .setMessage(errorDescription);
        return ResponseEntity.badRequest().body(acknowledgeResponse);
    }
    @ExceptionHandler(value = {BadCredentialsException.class, JOSEException.class, JwtExpiredException.class})
    public ResponseEntity<Object> handletUnAnauthorizeException(Exception e, WebRequest request){
        String errorDescription = e.getLocalizedMessage();
        if(Strings.isEmpty(errorDescription)){
            errorDescription = e.toString();
        }

        ErrorMessage errorMessage = new ErrorMessage()
                .setErrorMessageDescription(errorDescription)
                .setOffsetDateTime(OffsetDateTime.now());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }


}
