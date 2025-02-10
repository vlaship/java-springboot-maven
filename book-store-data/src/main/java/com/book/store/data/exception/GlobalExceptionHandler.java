package com.book.store.data.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ProblemDetail> notFound(NotFoundException ex) {
        log.error("Not Found {} ID: {}", ex.getType(), ex.getId());
        var details = "Not Found %s ID: %s".formatted(ex.getType(), ex.getId().toString());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, details));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> handle(Exception ex) {
        log.error("error: {}", ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }
}
