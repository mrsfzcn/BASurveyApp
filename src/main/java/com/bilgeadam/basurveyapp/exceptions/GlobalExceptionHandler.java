package com.bilgeadam.basurveyapp.exceptions;

import com.bilgeadam.basurveyapp.exceptions.custom.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

import static com.bilgeadam.basurveyapp.exceptions.ExceptionType.*;

/**
 * @author Eralp Nitelik
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception exception) {
        log.error(messageSource.getMessage("exception.UNEXPECTED_ERROR=", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(UNEXPECTED_ERROR, exception);
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException exception) {
        log.error(messageSource.getMessage("exception.RUNTIME_ERROR", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(RUNTIME_EXCEPTION, exception);
    }

    @ResponseBody
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(UsernameNotFoundException exception) {
        String errorMessage = messageSource.getMessage("exception.LOGIN_ERROR_USERNAME_DOES_NOT_EXIST", null, Locale.getDefault());
        log.error(errorMessage, exception);
        return createExceptionInfoResponse(LOGIN_ERROR_USERNAME_DOES_NOT_EXIST, exception);
    }

    @ResponseBody
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException exception) {
        log.warn(messageSource.getMessage("exception.LOGIN_ERROR_WRONG_PASSWORD", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(LOGIN_ERROR_WRONG_PASSWORD, exception);
    }

    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        log.warn(messageSource.getMessage("exception.REGISTER_ERROR_DATA_EXISTS", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(REGISTER_ERROR_DATA_EXISTS, exception);
    }

    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        log.warn(messageSource.getMessage("exception.RESOURCE_NOT_FOUND", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(RESOURCE_NOT_FOUND, exception);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn(messageSource.getMessage("exception.DATA_NOT_VALID", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(DATA_NOT_VALID, exception);
    }

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException exception) {
        log.warn(messageSource.getMessage("exception.ACCESS_DENIED", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(ACCESS_DENIED, exception);
    }

    @ResponseBody
    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleQuestionNotFoundException(QuestionNotFoundException exception) {
        log.warn(messageSource.getMessage("exception.QUESTION_NOT_FOUND", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(QUESTION_NOT_FOUND, exception);
    }

    @ResponseBody
    @ExceptionHandler(QuestionTypeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleQuestionTypeNotFoundException(QuestionTypeNotFoundException exception) {
        log.warn(messageSource.getMessage("exception.QUESTION_TYPE_NOT_FOUND", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(QUESTION_TYPE_NOT_FOUND, exception);
    }

    @ResponseBody
    @ExceptionHandler(AlreadyAnsweredSurveyException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyAnsweredSurveyException(AlreadyAnsweredSurveyException exception) {
        log.warn(messageSource.getMessage("exception.SURVEY_ALREADY_ANSWERED", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(SURVEY_ALREADY_ANSWERED, exception);
    }

    @ResponseBody
    @ExceptionHandler(QuestionsAndResponsesDoesNotMatchException.class)
    public ResponseEntity<ExceptionResponse> handleQuestionsAndResponsesDoesNotMatchException(QuestionsAndResponsesDoesNotMatchException exception) {
        log.warn(messageSource.getMessage("exception.QUESTIONS_AND_RESPONSES_DOES_NOT_MATCH", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(QUESTIONS_AND_RESPONSES_DOES_NOT_MATCH, exception);
    }

    @ResponseBody
    @ExceptionHandler(UserInsufficientAnswerException.class)
    public ResponseEntity<ExceptionResponse> handleUserInsufficientAnswerException(UserInsufficientAnswerException exception) {
        log.warn(messageSource.getMessage("exception.USER_INSUFFICIENT_ANSWER", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(USER_INSUFFICIENT_ANSWER, exception);
    }

    @ResponseBody
    @ExceptionHandler(UserDoesNotExistsException.class)
    public ResponseEntity<ExceptionResponse> handleUserDoesNotExistsException(UserDoesNotExistsException exception) {
        log.warn(messageSource.getMessage("exception.USER_DOES_NOT_EXIST", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(USER_DOES_NOT_EXIST, exception);
    }

    @ResponseBody
    @ExceptionHandler(SurveyNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleSurveyNotFoundException(SurveyNotFoundException exception) {
        log.warn(messageSource.getMessage("exception.SURVEY_NOT_FOUND", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(SURVEY_NOT_FOUND, exception);
    }

    @ResponseBody
    @ExceptionHandler(ClassroomNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleClassroomNotFoundException(ClassroomNotFoundException exception) {
        log.warn(messageSource.getMessage("exception.CLASSROOM_NOT_FOUND", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(CLASSROOM_NOT_FOUND, exception);
    }

    @ResponseBody
    @ExceptionHandler(ClassroomExistException.class)
    public ResponseEntity<ExceptionResponse> handleClassroomExistException(ClassroomExistException exception) {
        log.warn(messageSource.getMessage("exception.CLASSROOM_ALREADY_EXISTS", null, Locale.getDefault()), exception);
        return createExceptionInfoResponse(CLASSROOM_ALREADY_EXISTS, exception);
    }

    //    private ResponseEntity<ExceptionResponse> createExceptionInfoResponse(ExceptionType exceptionType, Exception exception) {
//        return new ResponseEntity<>(ExceptionResponse.builder()
//                .exceptionCode(exceptionType.getCode())
//                .customMessage(exceptionType.getMessage())
//                .exceptionMessage(exception.getMessage())
//                .httpStatus(exceptionType.getHttpStatus().value())
//                .build(), exceptionType.getHttpStatus());
//    }
    private ResponseEntity<ExceptionResponse> createExceptionInfoResponse(ExceptionType exceptionType, Exception exception) {
        String exceptionName = exceptionType.name();
        String localizedMessage = messageSource.getMessage("exception." + exceptionName, null, Locale.getDefault());
        return new ResponseEntity<>(ExceptionResponse.builder()
                .exceptionCode(exceptionType.getCode())
                .customMessage(localizedMessage)
                .exceptionMessage(exception.getMessage())
                .httpStatus(exceptionType.getHttpStatus().value())
                .build(), exceptionType.getHttpStatus());
    }


}
