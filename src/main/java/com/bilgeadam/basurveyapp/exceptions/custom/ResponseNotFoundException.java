package com.bilgeadam.basurveyapp.exceptions.custom;

public class ResponseNotFoundException extends RuntimeException{
    public ResponseNotFoundException(String message) {
        super(message);
    }
}
