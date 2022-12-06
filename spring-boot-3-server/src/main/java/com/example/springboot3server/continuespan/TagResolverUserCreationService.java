package com.example.springboot3server.continuespan;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.springboot3server.entity.UserGroup;
import com.example.springboot3server.service.UserCreationService;
import io.micrometer.tracing.annotation.ContinueSpan;
import io.micrometer.tracing.annotation.SpanTag;
import io.micrometer.tracing.annotation.TagValueResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagResolverUserCreationService {

    private final UserCreationService userCreationService;

    @Transactional
    @ContinueSpan(log = "user.create")
    public UserGroup create(
            @SpanTag(value = "user.creation.request", resolver = TagValueResolver.class) final UserCreationRequest creationRequest
    ) {
        return userCreationService.create(creationRequest);
    }

}
