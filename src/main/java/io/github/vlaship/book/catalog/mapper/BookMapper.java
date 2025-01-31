package io.github.vlaship.book.catalog.mapper;

import io.github.vlaship.book.catalog.dto.BookListResponse;
import io.github.vlaship.book.catalog.dto.BookResponse;
import io.github.vlaship.book.catalog.dto.CreateBookRequest;
import io.github.vlaship.book.catalog.dto.UpdateBookRequest;
import io.github.vlaship.book.catalog.entity.Book;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN,
    unmappedSourcePolicy = ReportingPolicy.WARN,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookMapper {

  @Mapping(target = "storeIds", source = "storeIds")
  BookResponse map(Book entity, List<Long> storeIds);

  BookListResponse map(Book entity, String author);

  @Mapping(target = "id", ignore = true)
  Book map(CreateBookRequest dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "authorId", source = "authorId")
  void merge(UpdateBookRequest dto, @MappingTarget Book entity);

}
