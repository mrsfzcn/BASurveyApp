package com.bilgeadam.basurveyapp.exceptions.custom;

public class QuestionAlreadyExistsException extends RuntimeException {
    public QuestionAlreadyExistsException(String message) {
        super(message);
    }
}
