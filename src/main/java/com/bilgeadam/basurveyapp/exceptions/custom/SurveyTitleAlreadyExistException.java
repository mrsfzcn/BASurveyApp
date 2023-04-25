package com.bilgeadam.basurveyapp.exceptions.custom;

public class SurveyTitleAlreadyExistException extends RuntimeException{
    public SurveyTitleAlreadyExistException(String message){
        super(message);
    }
}
