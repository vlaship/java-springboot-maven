package com.book.store.facade.mapper;

import com.book.store.facade.model.*;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StoreMapper {

    StoreFacadeResponse map(StoreDataResponse data);

    List<StoreFacadeResponse> map(List<StoreDataResponse> data);

    CreateStoreDataRequest map(CreateStoreFacadeRequest facade);

    UpdateStoreDataRequest map(UpdateStoreFacadeRequest facade);
}
