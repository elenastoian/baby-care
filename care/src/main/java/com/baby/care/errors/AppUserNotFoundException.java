package com.baby.care.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppUserNotFoundException extends RuntimeException{
    private final HttpStatus status;

    public AppUserNotFoundException() {
        super("Failed to find AppUser.");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
