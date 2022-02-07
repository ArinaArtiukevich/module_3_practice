package com.esm.epam.controller;

import com.esm.epam.entity.ErrorResponse;
import com.esm.epam.exception.ControllerException;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(1, exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public final ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException exception) {
        ErrorResponse errorResponse = new ErrorResponse(2, exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        ErrorResponse errorResponse = new ErrorResponse(3, exception.getMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(ControllerException.class)
    public final ResponseEntity<ErrorResponse> handleControllerException(ControllerException exception) {
        ErrorResponse errorResponse = new ErrorResponse(4, exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DaoException.class)
    public final ResponseEntity<ErrorResponse> handleDaoException(DaoException exception) {
        ErrorResponse errorResponse = new ErrorResponse(5, exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }
}