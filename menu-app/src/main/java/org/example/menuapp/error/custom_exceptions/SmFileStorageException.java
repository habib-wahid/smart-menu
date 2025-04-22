package org.example.menuapp.error.custom_exceptions;

public class SmFileStorageException extends RuntimeException {
    public SmFileStorageException(String message) {
        super(message);
    }
}
