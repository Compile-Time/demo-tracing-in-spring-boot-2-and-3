package com.example.springboot3server.service;

import com.example.springboot3server.entity.User;
import com.example.springboot3server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(final String userName) {
        return userRepository.save(User.builder()
                .name(userName)
                .build());
    }

    public Optional<User> findByUserName(final String userName) {
        return userRepository.findByName(userName);
    }

}
