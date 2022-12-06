package com.example.springboot2client.controller;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.commoninterface.user.UserCreationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {

    private final Tracer tracer;
    private final RestTemplate restTemplate;

    private ResponseEntity<UserCreationResult> requestUserCreation(final String restEndpoint, final UserCreationRequest creationRequest) {
        log.info("Creating new user (with group) with the following data: {}", creationRequest);
        final var requestUrl = String.format("http://localhost:8080/%s", restEndpoint);

        final var response = restTemplate.postForEntity(requestUrl, creationRequest, UserCreationResult.class);
        Optional.ofNullable(response.getBody())
                .ifPresent(userAndGroup ->
                        log.info("User and group created: {} - {}", userAndGroup.userName(), userAndGroup.groupName())
                );

        return response;
    }

    @PostMapping("tracer")
    public ResponseEntity<UserCreationResult> createGroup(@RequestBody final UserCreationRequest creationRequest) {
        final Span newSpan = this.tracer.nextSpan().name("request new user for tracer sample");
        try (final var ignore = this.tracer.withSpan(newSpan)) {
            return requestUserCreation("tracer/users", creationRequest);
        } finally {
            newSpan.end();
        }
    }

    @PostMapping("tracer/current-span")
    public ResponseEntity<UserCreationResult> createGroupCurrentSpan(@RequestBody final UserCreationRequest creationRequest) {
        Optional.ofNullable(this.tracer.currentSpan())
                .ifPresent(span -> span.event("request new user for tracer with current span sample"));
        return requestUserCreation("tracer/users/current-span", creationRequest);
    }
}
