package org.example.menuapp.error.custom_exceptions;

public class SmResourceNotFoundException extends RuntimeException {
    public SmResourceNotFoundException(String message) {
        super(message);
    }
}
