package io.github.vlaship.book.catalog.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record BookResponse(
    Long id,
    String title,
    String isbn,
    Long authorId,
    List<Long> storeIds
) {

}
