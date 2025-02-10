package com.book.store.facade.service;

import com.book.store.facade.model.*;
import com.book.store.facade.mapper.AuthorMapper;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {

    @Value("${data-service.author.url}")
    private String bookUrl;

    private final AuthorMapper mapper;
    private final RestTemplate restTemplate;

    public AuthorFacadeResponse create(CreateAuthorFacadeRequest request) {
        log.debug("create({})", request);
        var req = mapper.map(request);
        var resp = restTemplate.exchange(
                bookUrl,
                HttpMethod.POST,
                new HttpEntity<>(req),
                AuthorDataResponse.class
        );
        return mapper.map(resp.getBody());
    }

    public AuthorFacadeResponse update(UUID id, UpdateAuthorFacadeRequest request) {
        log.debug("update({},{})", id, request);
        var req = mapper.map(request);
        var resp = restTemplate.exchange(
                bookUrl + "/" + id.toString(),
                HttpMethod.PATCH,
                new HttpEntity<>(req),
                AuthorDataResponse.class
        );
        return mapper.map(resp.getBody());
    }

    public void delete(UUID id) {
        log.debug("delete({})", id);
        restTemplate.delete(bookUrl + "/" + id.toString());
    }

    public List<AuthorFacadeResponse> findAll() {
        log.debug("findAll()");
        var resp = restTemplate.exchange(
                bookUrl,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<AuthorDataResponse>>() {
                }
        );
        return mapper.map(resp.getBody());
    }

    public AuthorFacadeResponse findById(UUID id) {
        log.debug("findById({})", id);
        var resp = restTemplate.exchange(
                bookUrl + "/" + id.toString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                AuthorDataResponse.class
        );
        return mapper.map(resp.getBody());
    }
}
