package com.baby.care.errors;

import org.springframework.http.HttpStatus;

public class BabyNotFoundException extends RuntimeException{
    private final HttpStatus status;

    public BabyNotFoundException(Long id) {
        super("Failed to find Baby with id " + id);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
