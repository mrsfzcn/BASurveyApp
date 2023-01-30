package com.bilgeadam.basurveyapp.exceptions.custom;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
