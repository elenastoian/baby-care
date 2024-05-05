package com.baby.care.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FailedToSaveBabyException extends RuntimeException {
    private final HttpStatus status;

    public FailedToSaveBabyException() {
        super("Failed to save baby.");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
