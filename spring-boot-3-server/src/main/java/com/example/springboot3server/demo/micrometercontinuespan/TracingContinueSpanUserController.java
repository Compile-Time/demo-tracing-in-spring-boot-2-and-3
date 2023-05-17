package com.example.springboot3server.demo.micrometercontinuespan;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.commoninterface.user.UserCreationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("micrometer/continue-span/users")
@RequiredArgsConstructor
public class TracingContinueSpanUserController {

    private final TracingContinueSpanUserCreationService tracingContinueSpanUserCreationService;
    private final TracingTagResolverUserCreationService tracingTagResolverUserCreationService;
    private final TracingExpressionUserCreationService tracingExpressionUserCreationService;

    @PostMapping
    public ResponseEntity<UserCreationResult> createUser(@RequestBody final UserCreationRequest creationRequest) {
        final var userGroup = tracingContinueSpanUserCreationService.create(creationRequest);
        return ResponseEntity.ok(UserCreationResult.builder()
                .userName(userGroup.getUser().getName())
                .groupName(userGroup.getGroup().getName())
                .build());
    }

    @PostMapping("/tag-resolver")
    public ResponseEntity<UserCreationResult> createUserSpanTagResolver(@RequestBody final UserCreationRequest creationRequest) {
        final var userGroup = tracingTagResolverUserCreationService.create(creationRequest);
        return ResponseEntity.ok(UserCreationResult.builder()
                .userName(userGroup.getUser().getName())
                .groupName(userGroup.getGroup().getName())
                .build());
    }

    @PostMapping("/expression")
    public ResponseEntity<UserCreationResult> createUserSpanExpression(@RequestBody final UserCreationRequest creationRequest) {
        final var userGroup = tracingExpressionUserCreationService.create(creationRequest);
        return ResponseEntity.ok(UserCreationResult.builder()
                .userName(userGroup.getUser().getName())
                .groupName(userGroup.getGroup().getName())
                .build());
    }

}
