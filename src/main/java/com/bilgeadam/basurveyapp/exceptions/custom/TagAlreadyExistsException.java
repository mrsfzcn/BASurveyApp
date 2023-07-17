package com.bilgeadam.basurveyapp.exceptions.custom;

public class TagAlreadyExistsException extends RuntimeException{
    public TagAlreadyExistsException(String message) {
        super(message);
    }
}
