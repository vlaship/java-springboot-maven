package com.book.store.facade.model;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateStoreFacadeRequest {
    @Size(min = 2, max = 50)
    private final String name;
    @Size(min = 2, max = 50)
    private final String address;
}
