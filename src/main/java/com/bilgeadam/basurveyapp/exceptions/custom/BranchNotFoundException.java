package com.bilgeadam.basurveyapp.exceptions.custom;

public class BranchNotFoundException extends RuntimeException{

    public BranchNotFoundException(String message) {
        super(message);
    }
}
