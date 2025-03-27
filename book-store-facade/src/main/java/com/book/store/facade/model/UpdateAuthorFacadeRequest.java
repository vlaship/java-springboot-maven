package com.book.store.facade.model;

import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAuthorFacadeRequest {
    @Size(min = 2, max = 50)
    private String name;
}
