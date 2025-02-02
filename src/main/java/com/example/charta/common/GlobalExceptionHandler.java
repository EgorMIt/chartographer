package com.example.charta.common;

import com.example.charta.errors.BadRequestException;
import com.example.charta.errors.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Обработчик ошибок
 *
 * @author Egor Mitrofanov
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Обработка исключений при обращении к несуществующему файлу
     *
     * @param ex исключение.
     * @return {@link NotFoundException}
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex) {
        return ex.getError();
    }

    /**
     * Обработка исключений при невалидных параметрах запроса
     *
     * @param ex исключение.
     * @return {@link BadRequestException}
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequestException(BadRequestException ex) {
        return ex.getError();
    }
}