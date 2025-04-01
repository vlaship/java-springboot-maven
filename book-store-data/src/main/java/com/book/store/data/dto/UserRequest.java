package com.book.store.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotNull
    @NotBlank
    @Size(min = 5, max = 50)
    private String username;
    @NotNull
    @NotBlank
    @Size(min = 12, max = 50)
    private String password;
    @NotNull
    @NotBlank
    @Size(min = 5, max = 50)
    private String email;
    @NotNull
    @Size(min = 1, max = 10)
    private Collection<String> roles;
}
