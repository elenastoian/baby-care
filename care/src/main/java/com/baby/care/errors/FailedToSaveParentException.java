package com.baby.care.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FailedToSaveParentException extends RuntimeException{
    private final HttpStatus status;

    public FailedToSaveParentException() {
        super("Failed to save parent.");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
