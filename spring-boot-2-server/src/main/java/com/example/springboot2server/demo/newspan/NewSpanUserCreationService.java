package com.example.springboot2server.demo.newspan;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.springboot2server.entity.UserGroup;
import com.example.springboot2server.service.UserCreationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewSpanUserCreationService {

    private final UserCreationService userCreationService;

    @Transactional
    @NewSpan("create new user and group (@NewSpan)")
    public UserGroup create(
            @SpanTag("user.creation.request") final UserCreationRequest creationRequest
    ) {
        return userCreationService.create(creationRequest);
    }

}
