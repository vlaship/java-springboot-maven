package com.book.store.facade.security;

import com.book.store.facade.mapper.UserMapper;
import com.book.store.facade.model.UserDataResponse;
import com.book.store.facade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDataResponse user = userService.findByUsername(username);
        return userMapper.toModel(user);
    }
}
