package com.book.store.data.mapper;

import com.book.store.data.dto.CreateStoreRequest;
import com.book.store.data.dto.StoreResponse;
import com.book.store.data.dto.UpdateStoreRequest;
import com.book.store.data.entity.Store;
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
public interface StoreMapper {

    StoreResponse map(Store entity);

    Store map(StoreResponse dto);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    Store map(CreateStoreRequest dto);

    @Mapping(target = "id", ignore = true)
    void merge(@MappingTarget Store entity, UpdateStoreRequest dto);
}
