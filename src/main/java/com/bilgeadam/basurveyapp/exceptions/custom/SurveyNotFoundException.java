package com.bilgeadam.basurveyapp.exceptions.custom;

public class SurveyNotFoundException extends RuntimeException{
    public SurveyNotFoundException(String message) {
        super(message);
    }
}
