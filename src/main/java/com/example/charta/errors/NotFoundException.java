package com.example.charta.errors;

import lombok.Getter;

/**
 * Ошибка Not Found
 *
 * @author Egor Mitrofanov
 */
@Getter
public class NotFoundException extends RuntimeException {

    private final String error;

    public NotFoundException(String error) {
        this.error = error;
    }
}
