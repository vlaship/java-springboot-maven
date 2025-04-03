package com.book.store.data.exception;

import com.book.store.data.dto.ErrorResponse;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

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

    @ExceptionHandler(NotFoundUserException.class)
    ResponseEntity<ErrorResponse> notFoundUser(NotFoundUserException ex) {
        log.error("Not Found User: {}", ex.getUsername());
        String details = String.format("Not Found User: %s", ex.getUsername());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder().status(HttpStatus.NOT_FOUND).details(details).build());
    }

    @ExceptionHandler(AlreadyTakenException.class)
    ResponseEntity<ErrorResponse> alreadyTaken(AlreadyTakenException ex) {
        log.error("{}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().status(HttpStatus.BAD_REQUEST).details(ex.getMessage()).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("Validation error: {}", details);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().status(HttpStatus.BAD_REQUEST).details(details).build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String details = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        log.error("Constraint violation: {}", details);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().status(HttpStatus.BAD_REQUEST).details(details).build());
    }

    @ExceptionHandler(MismatchedInputException.class)
    ResponseEntity<ErrorResponse> handleMismatchedInputException(MismatchedInputException ex) {
        log.error("JSON parse error: {}", ex.getMessage());
        String details = "Invalid JSON format: " + ex.getMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().status(HttpStatus.BAD_REQUEST).details(details).build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Message not readable: {}", ex.getMessage());
        String details = "Invalid request format: " + ex.getMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().status(HttpStatus.BAD_REQUEST).details(details).build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());

        String message = ex.getMessage();
        String details;

        if (message != null && message.contains("constraint [users_username_key]")) {
            details = "Username already exists";
        } else {
            details = "Data integrity violation: " + message;
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().status(HttpStatus.BAD_REQUEST).details(details).build());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handle(Exception ex) {
        log.error("error: {}", ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(ErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).details(ex.getMessage()).build());
    }
}
