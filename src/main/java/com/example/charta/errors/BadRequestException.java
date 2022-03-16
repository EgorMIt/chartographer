package com.example.charta.errors;

import lombok.Getter;

/**
 * Ошибка Bad Request
 *
 * @author Egor Mitrofanov
 */
@Getter
public class BadRequestException extends RuntimeException {

    private final String error;

    public BadRequestException(String error) {
        this.error = error;
    }
}
