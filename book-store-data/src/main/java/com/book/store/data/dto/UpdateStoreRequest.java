package com.book.store.data.dto;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStoreRequest {
    @Size(min = 2, max = 50)
    private final String name;
    @Size(min = 2, max = 50)
    private final String address;
}
