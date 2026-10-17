package com.example.ticketingplatform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class EventsExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidEventException.class)
    public ResponseEntity<String> handleInvalidEventException(InvalidEventException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
