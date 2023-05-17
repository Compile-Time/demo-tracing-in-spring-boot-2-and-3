package com.example.springboot3server.demo.tracingnewspan;

import com.example.springboot3server.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TracingNewSpanUserService {

    private final TracingNewSpanUserRepository tracingNewSpanUserRepository;

    public Optional<User> findByName(final String userName) {
        return tracingNewSpanUserRepository.findByName(userName);
    }

}
