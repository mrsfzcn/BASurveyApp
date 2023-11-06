package com.bilgeadam.basurveyapp.exceptions.custom;

public class BranchAlreadyExistException extends RuntimeException{

    public BranchAlreadyExistException(String message) {
        super(message);
    }
}
