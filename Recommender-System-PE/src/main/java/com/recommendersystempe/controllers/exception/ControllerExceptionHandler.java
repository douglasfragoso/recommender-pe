package com.recommendersystempe.controllers.exception;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.recommendersystempe.service.exception.GeneralException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(GeneralException.class) 
    public ResponseEntity<StandardError> handleGeneralException(GeneralException e,
            HttpServletRequest request) {
        String error = "General Error";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationError> ConstraintViolationException(ConstraintViolationException ex,
            HttpServletRequest request) {
        String error = "Validation Error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = Collections.singletonList(ex.getSQLException().getMessage());

        ValidationError err = new ValidationError(Instant.now(), status.value(), error, errors,
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> MethodArgumentNotValidException(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String error = "Not Found";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ValidationError err = new ValidationError(Instant.now(), status.value(), error, errors,
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ValidationError> handleTypeMismatchException(MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        String error = "Validation Error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = Collections.singletonList("Type mismatch " + ex.getName());

        ValidationError err = new ValidationError(Instant.now(), status.value(), error, errors,
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ValidationError> handleNotFoundException(NoHandlerFoundException ex,
            HttpServletRequest request) {
        String error = "Validation Error";
        HttpStatus status = HttpStatus.NOT_FOUND;
        List<String> errors = Collections.singletonList("No handler found");

        ValidationError err = new ValidationError(Instant.now(), status.value(), error, errors,
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}