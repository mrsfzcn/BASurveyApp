package com.bilgeadam.basurveyapp.exceptions.custom;

public class QuestionTagExistException extends RuntimeException{
    public QuestionTagExistException(String message) {
        super(message);
    }
}
