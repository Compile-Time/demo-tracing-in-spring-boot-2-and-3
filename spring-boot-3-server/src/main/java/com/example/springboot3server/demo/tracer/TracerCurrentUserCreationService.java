package com.example.springboot3server.demo.tracer;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.springboot3server.entity.UserGroup;
import com.example.springboot3server.service.UserCreationService;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TracerCurrentUserCreationService {

    private final Tracer tracer;
    private final UserCreationService userCreationService;

    @Transactional
    public UserGroup create(final UserCreationRequest creationRequest) {
        final var currentSpan = this.tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.tag("user.name", creationRequest.userName());
            currentSpan.event(String.format("creation for user '%s' was requested", creationRequest.userName()));
        }
        return userCreationService.create(creationRequest);
    }

}
