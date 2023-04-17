package com.go.tokenverification.utils;

import com.go.tokenverification.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EmailValidationUtils {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^(.+)@(.+)$");
    private EmailValidationUtils(){

    }

    /**
     * Validate email
     * if email is empty
     *  return false
     * else
     *  check whether email is validate or not
     * @param email
     * @return
     */
    public static boolean validateEmail(String email){
        if(Strings.isEmpty(email)){
            return false;
        }
        final Matcher matcher = EMAIL_REGEX.matcher(email);
        return matcher.matches();
    }

}
;