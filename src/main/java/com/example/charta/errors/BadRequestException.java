package com.example.charta.errors;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final String error;

    public BadRequestException(String error) {
        this.error = error;
    }
}
