package com.samplecompany.exception;

import org.springframework.http.HttpStatus;


/* Defined an ApiError class for proper error responses */
public class ApiError {
    private final HttpStatus status;
    private final String message;

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters
    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}