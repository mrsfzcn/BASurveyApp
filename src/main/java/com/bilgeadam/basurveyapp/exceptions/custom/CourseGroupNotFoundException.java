package com.bilgeadam.basurveyapp.exceptions.custom;

public class CourseGroupNotFoundException extends RuntimeException{

    public CourseGroupNotFoundException(String message){
        super(message);
    }
}
