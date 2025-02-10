package com.book.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAuthorRequest(
    @NotBlank @Size(min = 2, max = 50)
    String name
) {

}
