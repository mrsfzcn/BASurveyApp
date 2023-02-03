package com.bilgeadam.basurveyapp.exceptions.custom;

public class UserInsufficientanswerException extends RuntimeException{

    public UserInsufficientanswerException(String message){
        super(message);
    }
}
