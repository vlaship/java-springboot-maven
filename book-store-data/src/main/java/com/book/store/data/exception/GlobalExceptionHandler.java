package com.book.store.data.exception;

import com.book.store.data.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ErrorResponse> notFound(NotFoundException ex) {
        log.error("Not Found {} ID: {}", ex.getType(), ex.getId());
        String details = String.format("Not Found %s ID: %s", ex.getType(), ex.getId().toString());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder().status(HttpStatus.NOT_FOUND).details(details).build());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handle(Exception ex) {
        log.error("error: {}", ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(ErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).details(ex.getMessage()).build());
    }
}
