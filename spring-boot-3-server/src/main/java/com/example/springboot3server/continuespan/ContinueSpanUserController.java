package com.example.springboot3server.continuespan;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.commoninterface.user.UserCreationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("continue-span/users")
@RequiredArgsConstructor
public class ContinueSpanUserController {

    private final ContinueSpanUserCreationService continueSpanUserCreationService;
    private final TagResolverUserCreationService tagResolverUserCreationService;
    private final ExpressionUserCreationService expressionUserCreationService;

    @PostMapping
    public ResponseEntity<UserCreationResult> createUser(@RequestBody final UserCreationRequest creationRequest) {
        final var userGroup = continueSpanUserCreationService.create(creationRequest);
        return ResponseEntity.ok(UserCreationResult.builder()
                .userName(userGroup.getUser().getName())
                .groupName(userGroup.getGroup().getName())
                .build());
    }

    @PostMapping("/tag-resolver")
    public ResponseEntity<UserCreationResult> createUserSpanTagResolver(@RequestBody final UserCreationRequest creationRequest) {
        final var userGroup = tagResolverUserCreationService.create(creationRequest);
        return ResponseEntity.ok(UserCreationResult.builder()
                .userName(userGroup.getUser().getName())
                .groupName(userGroup.getGroup().getName())
                .build());
    }

    @PostMapping("/expression")
    public ResponseEntity<UserCreationResult> createUserSpanExpression(@RequestBody final UserCreationRequest creationRequest) {
        final var userGroup = expressionUserCreationService.create(creationRequest);
        return ResponseEntity.ok(UserCreationResult.builder()
                .userName(userGroup.getUser().getName())
                .groupName(userGroup.getGroup().getName())
                .build());
    }

}
