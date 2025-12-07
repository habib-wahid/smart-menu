package org.example.menuapp.discount.domain.exception;

public class DiscountUsageLimitExceededException extends RuntimeException{
    public DiscountUsageLimitExceededException(String message) {
        super(message);
    }
}
