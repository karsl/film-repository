package com.github.karsl.filmrepository.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<?> handleConstraintViolationException(Exception e) {
        // Find the nested exception with cause ConstraintViolationException.
        while (!(e instanceof ConstraintViolationException)) {
            e = (Exception) e.getCause();
        }

        // Type of e is ConstraintViolationException by now.

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(((ConstraintViolationException) e).getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()));
    }

}
