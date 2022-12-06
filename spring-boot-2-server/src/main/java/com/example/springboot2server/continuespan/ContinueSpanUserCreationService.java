package com.example.springboot2server.continuespan;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.springboot2server.entity.UserGroup;
import com.example.springboot2server.service.UserCreationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.ContinueSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContinueSpanUserCreationService {

    private final UserCreationService userCreationService;

    @Transactional
    @ContinueSpan(log = "user.create")
    public UserGroup create(@SpanTag("user.creation.request") final UserCreationRequest creationRequest) {
        return userCreationService.create(creationRequest);
    }

}
