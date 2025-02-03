package com.book.store.mapper;

import com.book.store.dto.AuthorResponse;
import com.book.store.dto.CreateAuthorRequest;
import com.book.store.dto.UpdateAuthorRequest;
import com.book.store.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AuthorMapper {

  AuthorResponse map(Author entity);

  Author map(AuthorResponse dto);

  @Mapping(target = "id", ignore = true)
  Author map(CreateAuthorRequest dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", source = "name")
  void merge(@MappingTarget Author entity, UpdateAuthorRequest dto);
}
