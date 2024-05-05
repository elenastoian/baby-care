package com.baby.care.service.security;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailValidatorService {
    public boolean testIfEmailIsValid(String email) {

        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return patternMatches(email, regexPattern);
    }

    private boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}