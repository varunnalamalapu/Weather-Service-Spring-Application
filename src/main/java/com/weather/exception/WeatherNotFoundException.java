package com.weather.exception;

import org.springframework.http.HttpStatus;

public class WeatherNotFoundException extends RuntimeException {
    private HttpStatus errorCode;

    public WeatherNotFoundException(HttpStatus code) {
        this.errorCode = code;
    }
}
