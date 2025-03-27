package com.book.store.data.dto;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStoreRequest {
    @Size(min = 2, max = 50)
    private String name;
    @Size(min = 2, max = 50)
    private String address;
}
