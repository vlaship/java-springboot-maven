package com.book.store.data.dto;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAuthorRequest {
    @Size(min = 2, max = 50)
    private final String name;
}
