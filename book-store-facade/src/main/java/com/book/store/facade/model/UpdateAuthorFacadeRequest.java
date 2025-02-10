package com.book.store.facade.model;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAuthorFacadeRequest {
    @Size(min = 2, max = 50)
    private final String name;
}
