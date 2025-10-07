package com.example.projectrestfulapi.exception;

import lombok.Getter;

@Getter
public class InvalidException extends RuntimeException {
    private final NumberError numberError;

    public InvalidException(String message, NumberError numberError) {
        super(message);
        this.numberError = numberError;
    }
}
