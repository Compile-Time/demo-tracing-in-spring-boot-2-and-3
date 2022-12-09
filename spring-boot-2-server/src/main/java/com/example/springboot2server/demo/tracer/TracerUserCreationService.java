package com.example.springboot2server.demo.tracer;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.springboot2server.entity.UserGroup;
import com.example.springboot2server.service.UserCreationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TracerUserCreationService {

    private final Tracer tracer;
    private final UserCreationService userCreationService;

    @Transactional
    public UserGroup create(final UserCreationRequest creationRequest) {
        final var newSpan = this.tracer.nextSpan()
                .name(String.format("create new user %s", creationRequest.userName()));

        try (final var ignored = this.tracer.withSpan(newSpan.start())) {
            newSpan.tag("user.name", creationRequest.userName());
            newSpan.event("start creation of new user and group");

            return userCreationService.create(creationRequest);
        } finally {
            newSpan.event("end creation of new user and group");
            newSpan.end();
        }
    }

}
