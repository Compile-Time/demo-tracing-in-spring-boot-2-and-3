package com.example.springboot3server.demo.newspan;

import com.example.commoninterface.user.User;
import com.example.commoninterface.user.UserCreationRequest;
import com.example.commoninterface.user.UserCreationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/new-span/users")
@RequiredArgsConstructor
public class NewSpanUserController {

    private final NewSpanUserCreationService newSpanUserCreationService;
    private final NewSpanUserService newSpanUserService;

    @PostMapping
    public ResponseEntity<UserCreationResult> createUser(@RequestBody final UserCreationRequest creationRequest) {
        final var userGroup = newSpanUserCreationService.create(creationRequest);
        return ResponseEntity.ok(UserCreationResult.builder()
                .userName(userGroup.getUser().getName())
                .groupName(userGroup.getGroup().getName())
                .build());
    }

    @GetMapping("/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable("name") final String userName) {
        return newSpanUserService.findByName(userName)
                .map(user -> User.builder()
                        .name(user.getName())
                        .build())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
