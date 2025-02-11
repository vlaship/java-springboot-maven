package com.book.store.data.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorResponse {
    private final HttpStatus status;
    private final String details;
}
