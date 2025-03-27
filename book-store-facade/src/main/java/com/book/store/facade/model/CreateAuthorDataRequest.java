package com.book.store.facade.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuthorDataRequest {
    @Size(min = 2, max = 50)
    private String name;
}
