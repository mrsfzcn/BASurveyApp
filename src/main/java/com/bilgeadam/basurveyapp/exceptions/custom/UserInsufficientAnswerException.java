package com.bilgeadam.basurveyapp.exceptions.custom;

public class UserInsufficientAnswerException extends RuntimeException{

    public UserInsufficientAnswerException(String message){
        super(message);
    }
}
