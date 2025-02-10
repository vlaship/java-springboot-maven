package com.book.store.data.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateStoreRequest {
    @Size(min = 2, max = 50)
    private final String name;
    @Size(min = 2, max = 50)
    private final String address;
}
