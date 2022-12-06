package com.example.springboot2server.continuespan;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.springboot2server.entity.UserGroup;
import com.example.springboot2server.service.UserCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.annotation.ContinueSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.cloud.sleuth.annotation.TagValueResolver;
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
