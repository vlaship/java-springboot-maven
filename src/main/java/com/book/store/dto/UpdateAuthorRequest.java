package com.book.store.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAuthorRequest {
    @Size(min = 2, max = 50)
    private final String name;
}
