package com.springboot.rest.api.server.exception;

import org.springframework.http.HttpStatus;

public class MyAPIException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public MyAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public MyAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
