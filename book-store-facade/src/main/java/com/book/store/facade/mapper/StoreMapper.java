package com.book.store.facade.mapper;

import com.book.store.facade.model.*;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class StoreMapper {

    public StoreFacadeResponse map(StoreDataResponse data) {
        if (data == null) {
            return null;
        }

        UUID id = data.getId();
        String name = data.getName();
        String address = data.getAddress();

        return new StoreFacadeResponse(id, name, address);
    }

    public List<StoreFacadeResponse> map(List<StoreDataResponse> data) {
        if (data == null) {
            return null;
        }

        List<StoreFacadeResponse> list = new ArrayList<StoreFacadeResponse>(data.size());
        for (StoreDataResponse storeDataResponse : data) {
            list.add(map(storeDataResponse));
        }

        return list;
    }

    public CreateStoreDataRequest map(CreateStoreFacadeRequest facade) {
        if (facade == null) {
            return null;
        }

        String name = facade.getName();
        String address = facade.getAddress();

        return new CreateStoreDataRequest(name, address);
    }

    public UpdateStoreDataRequest map(UpdateStoreFacadeRequest facade) {
        if (facade == null) {
            return null;
        }

        String name = facade.getName();
        String address = facade.getAddress();

        return new UpdateStoreDataRequest(name, address);
    }
}
