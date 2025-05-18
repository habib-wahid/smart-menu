package org.example.menuapp.error.custom_exceptions;

public class SmUpdateNotAllowedException extends RuntimeException{
    public SmUpdateNotAllowedException(String message){
        super(message);
    }
}
