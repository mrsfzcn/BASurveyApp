package com.bilgeadam.basurveyapp.exceptions.custom;

public class StudentTagExistException extends RuntimeException{
    public StudentTagExistException(String message) {
        super(message);
    }
}
