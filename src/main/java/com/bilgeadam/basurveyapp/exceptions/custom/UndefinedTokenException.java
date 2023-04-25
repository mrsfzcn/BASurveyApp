package com.bilgeadam.basurveyapp.exceptions.custom;

public class UndefinedTokenException extends RuntimeException{
    public UndefinedTokenException(String message){
        super(message);
    }
}
