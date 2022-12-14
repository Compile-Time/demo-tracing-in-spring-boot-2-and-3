package com.example.springboot3server.demo.tracer;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.springboot3server.entity.UserGroup;
import com.example.springboot3server.service.UserCreationService;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                .name(String.format(
                        "Create new user '%s' with group '%s'",
                        creationRequest.userName(), creationRequest.groupName()
                ));

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
