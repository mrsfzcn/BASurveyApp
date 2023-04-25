package com.bilgeadam.basurveyapp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * @author Eralp Nitelik
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionType {
    /*
        Unknown errors.
     */
    UNEXPECTED_ERROR(9000, "Unexpected Error! Please submit a report.", INTERNAL_SERVER_ERROR),
    RUNTIME_EXCEPTION(9001, "Unhandled runtime error occurred!", INTERNAL_SERVER_ERROR),

    /*
        General errors.
     */
    INTERNAL_ERROR(9002, "Internal Server Error", INTERNAL_SERVER_ERROR),
    BAD_REQUEST_ERROR(9003, "Invalid Parameter Error", BAD_REQUEST),
    RESOURCE_NOT_FOUND(9004, "Resource is not Found", NOT_FOUND),
    RESPONSE_NOT_FOUND(9005, "Response is not Found", BAD_REQUEST),

    QUESTION_NOT_FOUND(9006, "Question is not Found", BAD_REQUEST),
    QUESTION_ALREADY_EXISTS(9007, "Question with the same question string already exists.", BAD_REQUEST),
    CLASSROOM_NOT_FOUND(9008, "Classroom is not found", BAD_REQUEST),
    CLASSROOM_ALREADY_EXISTS(9009, "Classroom is already exists", INTERNAL_SERVER_ERROR),
    SURVEY_ALREADY_ANSWERED(9010, "This user already has answers for this survey.", INTERNAL_SERVER_ERROR),
    USER_DOES_NOT_EXIST(9011, "No such user.", BAD_REQUEST),
    USER_INSUFFICIENT_ANSWER(9012, "User must answer all the questions.", BAD_REQUEST),
    QUESTIONS_AND_RESPONSES_DOES_NOT_MATCH(9013, "Questions and responses does not matches", BAD_REQUEST),
    QUESTION_TYPE_NOT_FOUND(9014, "Question type is not Found", BAD_REQUEST),
    SURVEY_NOT_FOUND(9015, "Survey is not found", BAD_REQUEST),
    ROLE_ALREADY_EXISTS(9016, "Role is already exists", INTERNAL_SERVER_ERROR),
    ROLE_NOT_FOUND(9017, "Role is not found", BAD_REQUEST),

    STUDENT_TAG_ALREADY_EXISTS(9018, "Student Tag is already exists", INTERNAL_SERVER_ERROR),
    QUESTION_TAG_ALREADY_EXISTS(9019, "Question Tag is already exists", INTERNAL_SERVER_ERROR),
    TRAINER_TAG_ALREADY_EXISTS(9020, "Trainer Tag is already exists", INTERNAL_SERVER_ERROR),
    SURVEY_TAG_ALREADY_EXISTS(9021, "Survey Tag is already exists", INTERNAL_SERVER_ERROR),
    TRAINER_TAG_NOT_FOUND(9022, "Trainer Tag not found", INTERNAL_SERVER_ERROR),
    TRAINER_NOT_FOUND(9023, "Trainer not found", INTERNAL_SERVER_ERROR),
    SURVEY_TAG_NOT_FOUND(9024, "Survey Tag not found", INTERNAL_SERVER_ERROR),
    QUESTION_TAG_NOT_FOUND(9025, "Question Tag not found", INTERNAL_SERVER_ERROR),

    /*
        Validation errors.
     */
    DATA_NOT_VALID(1001, "Data does not meet requirements", BAD_REQUEST),

    /*
        Authentication errors.
     */
    ACCESS_DENIED(2000, "Access denied.", UNAUTHORIZED),
    LOGIN_ERROR_USERNAME_DOES_NOT_EXIST(2001, "Username does not exist.", NOT_FOUND),
    LOGIN_ERROR_WRONG_PASSWORD(2002, "Wrong password.", BAD_REQUEST),

    /*
        Register errors.
     */
    REGISTER_ERROR_DATA_EXISTS(3001, "Data already exists.", INTERNAL_SERVER_ERROR);

    private int code;
    private String message;
    private HttpStatus httpStatus;
}
