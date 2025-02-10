package com.book.store.facade.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpStatusCodeException.class)
    ResponseEntity<ProblemDetail> handle(HttpStatusCodeException ex) {
        log.error("error: {}", ex.getMessage());
        var httpStatus = HttpStatus.valueOf(ex.getStatusCode().value());
        return ResponseEntity
                .status(httpStatus)
                .body(ProblemDetail.forStatusAndDetail(httpStatus, ex.getMessage()));
    }
}
