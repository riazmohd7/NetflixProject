package com.netflix.exceptions;

public class InvalidDataException extends RuntimeException{
    private String message;
    public InvalidDataException(final String message){
        this.message = message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
