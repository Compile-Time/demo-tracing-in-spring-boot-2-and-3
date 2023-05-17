package com.example.springboot3server.demo.micrometercontinuespan;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.springboot3server.entity.UserGroup;
import com.example.springboot3server.service.UserCreationService;
import io.micrometer.common.annotation.ValueResolver;
import io.micrometer.tracing.annotation.ContinueSpan;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TracingTagResolverUserCreationService {

    private final UserCreationService userCreationService;

    @Transactional
    @ContinueSpan(log = "user.create")
    public UserGroup create(
            @SpanTag(value = "user.creation.request", resolver = ValueResolver.class) final UserCreationRequest creationRequest
    ) {
        return userCreationService.create(creationRequest);
    }

}
