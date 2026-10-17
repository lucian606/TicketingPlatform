package com.example.ticketingplatform.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class EventsExceptionHandler {

    @ExceptionHandler(InvalidEventException.class)
    public ResponseEntity<ErrorMessage> handleInvalidEventException(InvalidEventException ex) {
        return ResponseEntity.badRequest().body(new ErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new ErrorMessage("Invalid request body: " + ex.getMessage()));
    }
}
