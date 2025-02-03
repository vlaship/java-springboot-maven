package com.book.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateStoreRequest(
    @NotBlank @Size(min = 2, max = 50)
    String name,
    @NotBlank @Size(min = 2, max = 50)
    String address
) {

}
