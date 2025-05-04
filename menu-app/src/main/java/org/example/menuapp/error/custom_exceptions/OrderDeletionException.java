package org.example.menuapp.error.custom_exceptions;

public class OrderDeletionException extends RuntimeException {
    public OrderDeletionException(String message) {
        super(message);
    }
}
