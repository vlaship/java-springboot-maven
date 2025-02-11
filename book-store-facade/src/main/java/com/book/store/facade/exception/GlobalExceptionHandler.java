package com.book.store.facade.exception;

import com.book.store.facade.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpStatusCodeException.class)
    ResponseEntity<ErrorResponse> handle(HttpStatusCodeException ex) {
        log.error("error: {}", ex.getMessage());
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode().value());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .details(ex.getMessage())
                .status(httpStatus)
                .build();
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponse);
    }
}
