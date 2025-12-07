package org.example.menuapp.discount.domain.exception;

public class DiscountConditionNotMatchException extends RuntimeException{
    public DiscountConditionNotMatchException(String message) {
        super(message);
    }
}
