package com.bilgeadam.basurveyapp.exceptions.custom;

/**
 * @author Taha Yasin CINAR
 */
public class RoleAlreadyExistException extends RuntimeException{
    public RoleAlreadyExistException(String message) {
        super(message);
    }
}
