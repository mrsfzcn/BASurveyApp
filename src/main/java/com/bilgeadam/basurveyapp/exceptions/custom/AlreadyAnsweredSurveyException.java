package com.bilgeadam.basurveyapp.exceptions.custom;

/**
 * @author Eralp Nitelik
 */
public class AlreadyAnsweredSurveyException extends RuntimeException{
    public AlreadyAnsweredSurveyException(String message) {
        super(message);
    }
}
