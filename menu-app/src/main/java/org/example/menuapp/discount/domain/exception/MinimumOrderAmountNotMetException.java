package org.example.menuapp.discount.domain.exception;

public class MinimumOrderAmountNotMetException extends  RuntimeException {
    public MinimumOrderAmountNotMetException(String message) {
        super(message);
    }
}
