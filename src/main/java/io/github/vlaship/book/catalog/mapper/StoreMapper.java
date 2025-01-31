package io.github.vlaship.book.catalog.mapper;

import io.github.vlaship.book.catalog.dto.CreateStoreRequest;
import io.github.vlaship.book.catalog.dto.StoreResponse;
import io.github.vlaship.book.catalog.dto.UpdateStoreRequest;
import io.github.vlaship.book.catalog.entity.Store;
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
public interface StoreMapper {

  StoreResponse map(Store entity);

  Store map(StoreResponse dto);

  @Mapping(target = "id", ignore = true)
  Store map(CreateStoreRequest dto);

  @Mapping(target = "id", ignore = true)
  void merge(@MappingTarget Store entity, UpdateStoreRequest dto);
}
