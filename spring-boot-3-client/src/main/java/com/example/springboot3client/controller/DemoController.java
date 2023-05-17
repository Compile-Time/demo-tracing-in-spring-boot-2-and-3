package com.example.springboot3client.controller;

import com.example.commoninterface.file.FileCreationRequest;
import com.example.commoninterface.file.FileCreationResponse;
import com.example.commoninterface.user.User;
import com.example.commoninterface.user.UserCreationRequest;
import com.example.commoninterface.user.UserCreationResult;
import com.example.springboot3client.config.RestTemplateProperties;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
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

    private final RestTemplateProperties restTemplateProperties;

    private final Tracer tracer;
    private final RestTemplate restTemplate;
    private final ObservationRegistry observationRegistry;

    private ResponseEntity<UserCreationResult> requestUserCreation(final String restEndpoint, final UserCreationRequest creationRequest) {
        log.info("Creating new user (with group) with the following data: {}", creationRequest);
        final var requestUrl = String.format("%s/%s", restTemplateProperties.getUrl(), restEndpoint);

        final var response = restTemplate.postForEntity(requestUrl, creationRequest, UserCreationResult.class);
        Optional.ofNullable(response.getBody())
                .ifPresent(userAndGroup ->
                        log.info("User and group created: {} - {}", userAndGroup.userName(), userAndGroup.groupName())
                );

        return response;
    }

    private ResponseEntity<FileCreationResponse> requestFileCreation(final String restEndpoint,
                                                                     final FileCreationRequest creationRequest) {
        log.info("Creating new file with the following data: {}", creationRequest);
        final var requestUrl = String.format("%s/%s", restTemplateProperties.getUrl(), restEndpoint);

        final var response = restTemplate.postForEntity(requestUrl, creationRequest, FileCreationResponse.class);
        Optional.ofNullable(response.getBody())
                .ifPresent(fileResponse ->
                        log.info("File data: {}", fileResponse)
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

        final var userResponse = restTemplate.getForEntity(String.format("%s/%s/%s", restTemplateProperties.getUrl(), "new-span/users", userName),
                User.class);
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

    @PostMapping("observation")
    public ResponseEntity<FileCreationResponse> demoObservation(@RequestBody final FileCreationRequest creationRequest) {
        return Observation.createNotStarted("request.file.creation", observationRegistry)
                .contextualName(String.format("request creation of file for user '%s' and group '%s' (Observation API)",
                        creationRequest.userName(), creationRequest.groupName()))
                .observe(() -> requestFileCreation("observation/files", creationRequest));
    }

    @PostMapping("observation-scope")
    public ResponseEntity<FileCreationResponse> demoObservationScoped(@RequestBody final FileCreationRequest creationRequest) {
        final var observation = Observation.start("request.file.creation", observationRegistry);

        try (final var ignored = observation.openScope()) {
            observation.contextualName(
                    String.format("request creation of file for user '%s' and group '%s' (Observation scope)",
                            creationRequest.userName(), creationRequest.groupName()));
            observation.event(Observation.Event.of("start request file creation"));

            return requestFileCreation("observation-scope/files", creationRequest);
        } finally {
            observation.event(Observation.Event.of("end request file creation"));
            observation.stop();
        }
    }

    @PostMapping("observation/convention")
    public ResponseEntity<FileCreationResponse> demoObservationWithConvention(@RequestBody final FileCreationRequest creationRequest) {
        return Observation.createNotStarted("request.file.creation", observationRegistry)
                .contextualName(
                        String.format("request creation of file for user '%s' and group '%s' (Observation with Convention)",
                                creationRequest.userName(), creationRequest.groupName()))
                .observe(() -> requestFileCreation("observation/files/convention", creationRequest));
    }

    @PostMapping("observation/custom-convention")
    public ResponseEntity<FileCreationResponse> demoObservationWithCustomConvention(@RequestBody final FileCreationRequest creationRequest) {
        return Observation.createNotStarted("request.file.creation", observationRegistry)
                .contextualName("observation with custom convention")
                .observe(() -> requestFileCreation("observation/files/custom-convention", creationRequest));
    }

    @PostMapping("observation-documentation")
    public ResponseEntity<FileCreationResponse> demoObservationDocumentation(@RequestBody final FileCreationRequest creationRequest) {
        return Observation.createNotStarted("request.file.creation", observationRegistry)
                .contextualName("observation documentation sample")
                .observe(() -> requestFileCreation("observation-documentation/files", creationRequest));
    }

    @PostMapping("observed")
    @Observed(
            name = "request.file.creation",
            contextualName = "observation created by observed annotation"
    )
    public ResponseEntity<FileCreationResponse> demoObservedAnnotation(@RequestBody final FileCreationRequest creationRequest) {
        return requestFileCreation("observed/files", creationRequest);
    }

    @PostMapping("micrometer/new-span")
    @NewSpan(name = "request new user for Micrometer @NewSpan sample")
    public ResponseEntity<UserCreationResult> demoSpanCreationWithMicrometerNewSpan(@RequestBody final UserCreationRequest creationRequest) {
        return requestUserCreation("micrometer/new-span/users", creationRequest);
    }

    @PostMapping("micrometer/continue-span")
    public ResponseEntity<UserCreationResult> demoMicrometerContinueSpan(@RequestBody final UserCreationRequest creationRequest) {
        return requestUserCreation("micrometer/continue-span/users", creationRequest);
    }

    @PostMapping("micrometer/continue-span/tag-resolver")
    public ResponseEntity<UserCreationResult> demoMicrometerSpanTagResolver(@RequestBody final UserCreationRequest creationRequest) {
        return requestUserCreation("micrometer/continue-span/users/tag-resolver", creationRequest);
    }

    @PostMapping("micrometer/continue-span/expression")
    public ResponseEntity<UserCreationResult> demoMicrometerSpanTagExpression(@RequestBody final UserCreationRequest creationRequest) {
        return requestUserCreation("micrometer/continue-span/users/expression", creationRequest);
    }

}
