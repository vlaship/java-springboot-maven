package com.book.store.facade.model;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStoreFacadeRequest {
    @Size(min = 2, max = 50)
    private final String name;
    @Size(min = 2, max = 50)
    private final String address;
}
