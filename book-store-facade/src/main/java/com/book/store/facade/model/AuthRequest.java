package com.book.store.facade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @NotNull
    @NotBlank
    @Size(min = 5, max = 50)
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 12, max = 50)
    private String password;
}