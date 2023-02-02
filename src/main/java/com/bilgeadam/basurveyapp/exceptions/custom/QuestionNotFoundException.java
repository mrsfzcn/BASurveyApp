package com.bilgeadam.basurveyapp.exceptions.custom;

public class QuestionNotFoundException extends RuntimeException{
    public QuestionNotFoundException(String message) {
        super(message);
    }
}
