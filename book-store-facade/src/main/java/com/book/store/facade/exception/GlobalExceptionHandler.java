package com.book.store.facade.exception;

import com.book.store.facade.model.ErrorResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(HttpStatusCodeException.class)
    ResponseEntity<ErrorResponse> handle(HttpStatusCodeException ex) {
        log.error("error: {}", ex.getMessage());
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode().value());
        String details = ex.getMessage();

        String resp = getDetails(ex);
        if (StringUtils.hasText(resp)) {
            details = resp;
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .details(details)
                .status(httpStatus)
                .build();
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponse);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    ResponseEntity<ErrorResponse> handle(InternalAuthenticationServiceException ex) {
        log.error("error: {}", ex.getMessage());

        ErrorResponse.ErrorResponseBuilder builder = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED);

        String message = ex.getMessage();

        HttpStatus status = getStatus(ex);
        if (status == HttpStatus.NOT_FOUND) {
            message = "Username Not Found";
        }

        ErrorResponse errorResponse = builder
                .details(message)
                .build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());

        StringBuilder details = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                details.append("Field '")
                        .append(error.getField())
                        .append("': value '")
                        .append(error.getRejectedValue())
                        .append("' ")
                        .append(error.getDefaultMessage())
                        .append("; ")
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(details.toString())
                .build();
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handle(Exception ex) {
        log.error("error: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .details(ex.getMessage())
                .build();
        return ResponseEntity
                .internalServerError()
                .body(errorResponse);
    }

    private String getDetails(HttpStatusCodeException ex) {
        String responseBody = ex.getResponseBodyAsString();
        try {
            // Try to parse the response body as JSON
            if (!responseBody.isEmpty()) {
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                // Extract details from the JSON if it exists
                if (jsonNode.has("details")) {
                    return jsonNode.get("details").asText();
                }
            }
        } catch (IOException e) {
            log.warn("Failed to parse response body as JSON: {}", responseBody, e);
        }
        return null;
    }

    private HttpStatus getStatus(InternalAuthenticationServiceException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof HttpClientErrorException) {
            HttpClientErrorException e = (HttpClientErrorException) cause;
            return HttpStatus.valueOf(e.getRawStatusCode());
        }
        return null;
    }
}
