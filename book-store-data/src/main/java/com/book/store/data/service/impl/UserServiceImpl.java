package com.book.store.data.service.impl;

import com.book.store.data.dto.UserRequest;
import com.book.store.data.dto.UserResponse;
import com.book.store.data.entity.User;
import com.book.store.data.exception.AlreadyTakenException;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.exception.NotFoundUserException;
import com.book.store.data.mapper.UserMapper;
import com.book.store.data.repository.UserRepository;
import com.book.store.data.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse create(UserRequest userRequest) {
        if (existsByUsername(userRequest.getUsername())) {
            throw new AlreadyTakenException("Username is already taken");
        }
        if (existsByEmail(userRequest.getEmail())) {
            throw new AlreadyTakenException("Email is already taken");
        }

        User user = userMapper.map(userRequest);
        User saved = userRepository.save(user);
        return userMapper.map(saved);
    }

    @Override
    public UserResponse update(UUID id, UserRequest userRequest) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User", id);
        }
        User user = userMapper.map(id, userRequest);
        User updated = userRepository.save(user);
        return userMapper.map(updated);
    }

    @Override
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundUserException(username));
        return userMapper.map(user);
    }

    @Override
    public UserResponse findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        return userMapper.map(user);
    }

    private boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
