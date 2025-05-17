package org.example.menuapp.error.custom_exceptions;

public class SmOrderProcessedException extends RuntimeException{
    public SmOrderProcessedException(String message){
        super(message);
    }
}
