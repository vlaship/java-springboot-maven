package com.book.store.facade.service;

import com.book.store.facade.model.*;
import com.book.store.facade.mapper.UserMapper;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${data-service.user.url}")
    private String userUrl;

    private final UserMapper mapper;
    private final RestTemplate restTemplate;

    public UserFacadeResponse create(CreateUserFacadeRequest request) {
        log.debug("create({})", request);
        CreateUserDataRequest req = mapper.map(request);
        ResponseEntity<UserDataResponse> resp = restTemplate.exchange(
                userUrl,
                HttpMethod.POST,
                new HttpEntity<>(req),
                UserDataResponse.class
        );
        return mapper.map(resp.getBody());
    }

    public UserFacadeResponse update(UUID id, UpdateUserFacadeRequest request) {
        log.debug("update({},{})", id, request);
        UpdateUserDataRequest req = mapper.map(request);
        ResponseEntity<UserDataResponse> resp = restTemplate.exchange(
                userUrl + "/" + id.toString(),
                HttpMethod.PATCH,
                new HttpEntity<>(req),
                UserDataResponse.class
        );
        return mapper.map(resp.getBody());
    }

    public void delete(UUID id) {
        log.debug("delete({})", id);
        restTemplate.delete(userUrl + "/" + id.toString());
    }

    public UserFacadeResponse findById(UUID id) {
        log.debug("findById({})", id);
        ResponseEntity<UserDataResponse> resp = restTemplate.exchange(
                userUrl + "/" + id.toString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                UserDataResponse.class
        );
        return mapper.map(resp.getBody());
    }

    public UserDataResponse findByUsername(String username) {
        log.debug("findByUsername({})", username);
        ResponseEntity<UserDataResponse> resp = restTemplate.exchange(
                userUrl + "/username/" + username,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                UserDataResponse.class
        );
        return resp.getBody();
    }

    public boolean existsByUsername(String username) {
        log.debug("existsByUsername({})", username);
        ResponseEntity<Boolean> resp = restTemplate.exchange(
                userUrl + "/exists/" + username,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Boolean.class
        );
        return Boolean.TRUE.equals(resp.getBody());
    }
}