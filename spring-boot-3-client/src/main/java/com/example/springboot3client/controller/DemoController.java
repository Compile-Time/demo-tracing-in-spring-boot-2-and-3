package com.example.springboot3client.controller;

import com.example.commoninterface.user.User;
import com.example.commoninterface.user.UserCreationRequest;
import com.example.commoninterface.user.UserCreationResult;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {

    private static final String LOCAL_HOST = "http://localhost:8080";

    private final Tracer tracer;
    private final RestTemplate restTemplate;

    private ResponseEntity<UserCreationResult> requestUserCreation(final String restEndpoint, final UserCreationRequest creationRequest) {
        log.info("Creating new user (with group) with the following data: {}", creationRequest);
        final var requestUrl = String.format("%s/%s", LOCAL_HOST, restEndpoint);

        final var response = restTemplate.postForEntity(requestUrl, creationRequest, UserCreationResult.class);
        Optional.ofNullable(response.getBody())
                .ifPresent(userAndGroup ->
                        log.info("User and group created: {} - {}", userAndGroup.userName(), userAndGroup.groupName())
                );

        return response;
    }

    @PostMapping("tracer")
    public ResponseEntity<UserCreationResult> demoSpanCreationWithTracer(@RequestBody final UserCreationRequest creationRequest) {
        final Span newSpan = this.tracer.nextSpan().name("request new user for tracer sample");
        try (final var ignore = this.tracer.withSpan(newSpan)) {
            return requestUserCreation("tracer/users", creationRequest);
        } finally {
            newSpan.end();
        }
    }

    @PostMapping("tracer/current-span")
    public ResponseEntity<UserCreationResult> demoSpanModificationWithTracer(@RequestBody final UserCreationRequest creationRequest) {
        Optional.ofNullable(this.tracer.currentSpan())
                .ifPresent(span -> span.event("request new user for tracer with current span sample"));
        return requestUserCreation("tracer/users/current-span", creationRequest);
    }

    @PostMapping("new-span")
    @NewSpan(name = "request new user for @NewSpan sample")
    public ResponseEntity<UserCreationResult> demoSpanCreationWithNewSpan(@RequestBody final UserCreationRequest creationRequest) {
        return requestUserCreation("new-span/users", creationRequest);
    }

    @GetMapping("new-span/repository/{userName}")
    @NewSpan(name = "request new user for @NewSpan sample with repository span")
    public ResponseEntity<User> demoRepositorySpanCreation(@PathVariable("userName") final String userName) {
        log.info("Request user with name '{}'", userName);

        final var userResponse = restTemplate.getForEntity(String.format("%s/%s/%s", LOCAL_HOST, "new-span/users", userName), User.class);
        Optional.ofNullable(userResponse.getBody())
                .ifPresent(user -> log.info("Received user {}", user));

        return userResponse;
    }

    @PostMapping("continue-span")
    public ResponseEntity<UserCreationResult> demoContinueSpan(@RequestBody final UserCreationRequest creationRequest) {
        return requestUserCreation("continue-span/users", creationRequest);
    }

    @PostMapping("continue-span/tag-resolver")
    public ResponseEntity<UserCreationResult> demoSpanTagResolver(@RequestBody final UserCreationRequest creationRequest) {
        return requestUserCreation("continue-span/users/tag-resolver", creationRequest);
    }

    @PostMapping("continue-span/expression")
    public ResponseEntity<UserCreationResult> demoSpanTagExpression(@RequestBody final UserCreationRequest creationRequest) {
        return requestUserCreation("continue-span/users/expression", creationRequest);
    }
}
