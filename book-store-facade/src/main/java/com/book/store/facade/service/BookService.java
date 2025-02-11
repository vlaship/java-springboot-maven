package com.book.store.facade.service;

import com.book.store.facade.model.*;
import com.book.store.facade.mapper.BookMapper;

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
public class BookService {

    @Value("${data-service.book.url}")
    private String bookUrl;

    private final BookMapper mapper;
    private final RestTemplate restTemplate;

    public BookFacadeResponse create(CreateBookFacadeRequest request) {
        log.debug("create({})", request);
        CreateBookDataRequest req = mapper.map(request);
        ResponseEntity<BookDataResponse> resp = restTemplate.exchange(
                bookUrl,
                HttpMethod.POST,
                new HttpEntity<>(req),
                BookDataResponse.class
        );
        return mapper.map(resp.getBody());
    }

    public BookFacadeResponse update(UUID id, UpdateBookFacadeRequest request) {
        log.debug("update({},{})", id, request);
        UpdateBookDataRequest req = mapper.map(request);
        ResponseEntity<BookDataResponse> resp = restTemplate.exchange(
                bookUrl + "/" + id.toString(),
                HttpMethod.PATCH,
                new HttpEntity<>(req),
                BookDataResponse.class
        );
        return mapper.map(resp.getBody());
    }

    public void delete(UUID id) {
        log.debug("delete({})", id);
        restTemplate.delete(bookUrl + "/" + id.toString());
    }

    public BookFacadeResponse findById(UUID id) {
        log.debug("findById({})", id);
        ResponseEntity<BookDataResponse> resp = restTemplate.exchange(
                bookUrl + "/" + id.toString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                BookDataResponse.class
        );
        return mapper.map(resp.getBody());
    }

    public List<BookFacadeResponse> findBooksByAuthorId(UUID authorId) {
        log.debug("findBooksByAuthorId({})", authorId);
        ResponseEntity<List<BookDataResponse>> resp = restTemplate.exchange(
                bookUrl + "/author/" + authorId.toString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<BookDataResponse>>() {
                });
        return mapper.map(resp.getBody());
    }
}
