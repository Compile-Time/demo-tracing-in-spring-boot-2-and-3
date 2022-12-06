package com.example.springboot3server.newspan;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.springboot3server.entity.UserGroup;
import com.example.springboot3server.service.UserCreationService;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
