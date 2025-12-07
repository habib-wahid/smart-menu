package org.example.menuapp.discount.domain.exception;

public class DiscountExpiredException extends RuntimeException {
    public DiscountExpiredException(String message) {
        super(message);
    }
}
