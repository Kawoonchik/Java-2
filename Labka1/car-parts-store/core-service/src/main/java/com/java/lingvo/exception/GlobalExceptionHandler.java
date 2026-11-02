package com.java.lingvo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApplicationErrorResponse> handle(IllegalArgumentException ex) {
        log.error("IllegalArgumentException: ", ex);
        return new ResponseEntity<>(new ApplicationErrorResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApplicationErrorResponse> handle(AccessDeniedException ex) {
        log.error("AccessDeniedException: ", ex);
        return new  ResponseEntity<>(new ApplicationErrorResponse(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ApplicationErrorResponse> handle(CardNotFoundException ex) {
        log.error("CardNotFoundException: ", ex);
        return new ResponseEntity<>(new ApplicationErrorResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeckNotFoundException.class)
    public ResponseEntity<ApplicationErrorResponse> handle(DeckNotFoundException ex) {
        log.error("DeckNotFoundException: ", ex);
        return new ResponseEntity<>(new ApplicationErrorResponse(ex), HttpStatus.NOT_FOUND);
    }
}
