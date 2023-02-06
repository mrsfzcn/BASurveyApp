package com.bilgeadam.basurveyapp.exceptions.custom;

public class QuestionsAndResponsesDoesNotMatchException extends RuntimeException{

    public QuestionsAndResponsesDoesNotMatchException(String message){
        super(message);
    }
}
