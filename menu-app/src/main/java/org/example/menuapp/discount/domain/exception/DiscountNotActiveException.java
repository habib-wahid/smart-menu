package org.example.menuapp.discount.domain.exception;

public class DiscountNotActiveException extends RuntimeException {
    public DiscountNotActiveException(String message) {
        super(message);
    }
}
