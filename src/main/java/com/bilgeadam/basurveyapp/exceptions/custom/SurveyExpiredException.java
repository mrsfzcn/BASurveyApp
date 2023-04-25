package com.bilgeadam.basurveyapp.exceptions.custom;

public class SurveyExpiredException extends RuntimeException{
    public SurveyExpiredException(String message){
        super(message);
    }
}
