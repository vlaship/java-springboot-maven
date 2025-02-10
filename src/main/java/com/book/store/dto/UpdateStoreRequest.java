package com.book.store.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStoreRequest {
    @Size(min = 2, max = 50)
    private final String name;
    @Size(min = 2, max = 50)
    private final String address;
}
