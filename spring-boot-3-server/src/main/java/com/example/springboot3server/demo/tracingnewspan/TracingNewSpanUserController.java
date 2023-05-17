package com.example.springboot3server.demo.tracingnewspan;

import com.example.commoninterface.user.User;
import com.example.commoninterface.user.UserCreationRequest;
import com.example.commoninterface.user.UserCreationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("micrometer/new-span/users")
@RequiredArgsConstructor
public class TracingNewSpanUserController {

    private final TracingNewSpanUserCreationService tracingNewSpanUserCreationService;
    private final TracingNewSpanUserService tracingNewSpanUserService;

    @PostMapping
    public ResponseEntity<UserCreationResult> createUser(@RequestBody final UserCreationRequest creationRequest) {
        final var userGroup = tracingNewSpanUserCreationService.create(creationRequest);
        return ResponseEntity.ok(UserCreationResult.builder()
                .userName(userGroup.getUser().getName())
                .groupName(userGroup.getGroup().getName())
                .build());
    }

    @GetMapping("/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable("name") final String userName) {
        return tracingNewSpanUserService.findByName(userName)
                .map(user -> User.builder()
                        .name(user.getName())
                        .build())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
