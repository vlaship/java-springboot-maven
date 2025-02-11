package com.book.store.facade.service;

import com.book.store.facade.model.*;
import com.book.store.facade.mapper.StoreMapper;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    @Value("${data-service.store.url}")
    private String bookUrl;

    private final StoreMapper mapper;
    private final RestTemplate restTemplate;

    public StoreFacadeResponse create(CreateStoreFacadeRequest request) {
        log.debug("create({})", request);
        CreateStoreDataRequest req = mapper.map(request);
        ResponseEntity<StoreDataResponse> resp = restTemplate.exchange(
                bookUrl,
                HttpMethod.POST,
                new HttpEntity<>(req),
                StoreDataResponse.class
        );
        return mapper.map(resp.getBody());
    }

    public StoreFacadeResponse update(UUID id, UpdateStoreFacadeRequest request) {
        log.debug("update({},{})", id, request);
        UpdateStoreDataRequest req = mapper.map(request);
        ResponseEntity<StoreDataResponse> resp = restTemplate.exchange(
                bookUrl + "/" + id.toString(),
                HttpMethod.PATCH,
                new HttpEntity<>(req),
                StoreDataResponse.class
        );
        return mapper.map(resp.getBody());
    }

    public void delete(UUID id) {
        log.debug("delete({})", id);
        restTemplate.delete(bookUrl + "/" + id.toString());
    }

    public List<StoreFacadeResponse> findAll() {
        log.debug("findAll()");
        ResponseEntity<List<StoreDataResponse>> resp = restTemplate.exchange(
                bookUrl,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<StoreDataResponse>>() {
                }
        );
        return mapper.map(resp.getBody());
    }

    public StoreFacadeResponse findById(UUID id) {
        log.debug("findById({})", id);
        ResponseEntity<StoreDataResponse> resp = restTemplate.exchange(
                bookUrl + "/" + id.toString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                StoreDataResponse.class
        );
        return mapper.map(resp.getBody());
    }
}