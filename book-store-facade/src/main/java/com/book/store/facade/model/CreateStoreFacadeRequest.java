package com.book.store.facade.model;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoreFacadeRequest {
    @Size(min = 2, max = 50)
    private String name;
    @Size(min = 2, max = 50)
    private String address;
}
