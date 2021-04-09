package com.weather;

import com.weather.exception.BadRequestException;
import com.weather.exception.WeatherNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity handleWeatherException(BadRequestException exception) {
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    @ExceptionHandler(value = {WeatherNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    protected ResponseEntity handleNotFoundException(WeatherNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND).build();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    protected ResponseEntity<String> handleException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), INTERNAL_SERVER_ERROR);
    }

}
