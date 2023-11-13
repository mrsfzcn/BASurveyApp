package com.bilgeadam.basurveyapp.exceptions.custom;

public class CourseAlreadyExistException extends RuntimeException{
    public CourseAlreadyExistException(String message) {
        super(message);
    }
}
