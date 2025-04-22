package org.example.menuapp.error.handler;

import org.example.menuapp.error.custom_exceptions.SmFileStorageException;
import org.example.menuapp.error.custom_exceptions.SmResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SmResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(SmResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SmFileStorageException.class)
    public ResponseEntity<Map<String, String>> handleFileStorageException(SmFileStorageException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("error", "File upload failed");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
