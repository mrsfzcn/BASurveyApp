package com.bilgeadam.basurveyapp.exceptions.custom;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
