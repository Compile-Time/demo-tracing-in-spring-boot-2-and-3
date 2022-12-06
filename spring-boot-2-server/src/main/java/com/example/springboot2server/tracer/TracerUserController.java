package com.example.springboot2server.tracer;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.commoninterface.user.UserCreationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tracer/users")
@RequiredArgsConstructor
public class TracerUserController {

    private final TracerUserCreationService tracerUserCreationService;
    private final TracerCurrentUserCreationService tracerCurrentUserCreationService;

    @PostMapping
    public ResponseEntity<UserCreationResult> createUser(@RequestBody final UserCreationRequest creationRequest) {
        final var userGroup = tracerUserCreationService.create(creationRequest);
        return ResponseEntity.ok(UserCreationResult.builder()
                .userName(userGroup.getUser().getName())
                .groupName(userGroup.getGroup().getName())
                .build());
    }

    @PostMapping("/current-span")
    public ResponseEntity<UserCreationResult> createUserCurrentSpan(@RequestBody final UserCreationRequest creationRequest) {
        final var userGroup = tracerCurrentUserCreationService.create(creationRequest);
        return ResponseEntity.ok(UserCreationResult.builder()
                .userName(userGroup.getUser().getName())
                .groupName(userGroup.getGroup().getName())
                .build());
    }

}
