package com.bilgeadam.basurveyapp.exceptions.custom;

/**
 * @author Eralp Nitelik
 */
public class UserDoesNotExistsException extends RuntimeException{
    public UserDoesNotExistsException(String message) {
        super(message);
    }
}
