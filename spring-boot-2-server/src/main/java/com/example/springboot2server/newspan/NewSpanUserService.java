package com.example.springboot2server.newspan;

import com.example.springboot2server.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewSpanUserService {

    private final NewSpanUserRepository newSpanUserRepository;

    public Optional<User> findByName(final String userName) {
        return newSpanUserRepository.findByName(userName);
    }

}
