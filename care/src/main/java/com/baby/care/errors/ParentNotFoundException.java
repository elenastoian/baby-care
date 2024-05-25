package com.baby.care.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ParentNotFoundException extends RuntimeException {
    private final HttpStatus status;

    public ParentNotFoundException() {
        super("Parent do not exist.");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
