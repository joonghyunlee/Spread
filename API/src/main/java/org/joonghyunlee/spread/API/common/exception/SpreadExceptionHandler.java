package org.joonghyunlee.spread.API.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class SpreadExceptionHandler {

    @ExceptionHandler(SpreadException.class)
    public ResponseEntity<SpreadExceptionResponse> handler(SpreadException e) {
        SpreadExceptionResponse response = new SpreadExceptionResponse(e.getMessage());

        return new ResponseEntity<>(response, e.getCode());
    }
}
