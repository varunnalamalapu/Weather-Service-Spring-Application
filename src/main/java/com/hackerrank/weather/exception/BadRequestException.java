package com.hackerrank.weather.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {
    private HttpStatus errorCode;

    public BadRequestException(HttpStatus code) {
        this.errorCode = code;
    }
}
