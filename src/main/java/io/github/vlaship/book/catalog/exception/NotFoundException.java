package io.github.vlaship.book.catalog.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException {
  private final String type;
  private final Long id;
}
