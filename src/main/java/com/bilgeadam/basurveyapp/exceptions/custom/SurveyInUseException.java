package com.bilgeadam.basurveyapp.exceptions.custom;

public class SurveyInUseException extends RuntimeException{

    public SurveyInUseException(String message) {
        super(message);
    }

}
