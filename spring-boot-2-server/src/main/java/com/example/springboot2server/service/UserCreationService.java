package com.example.springboot2server.service;

import com.example.commoninterface.user.UserCreationRequest;
import com.example.springboot2server.entity.UserGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCreationService {

    private final UserService userService;
    private final GroupService groupService;
    private final UserGroupService userGroupService;

    @Transactional
    public UserGroup create(final UserCreationRequest creationRequest) {
        final var groupName = Optional.ofNullable(creationRequest.groupName())
                .filter(name -> !name.isBlank())
                .orElse(creationRequest.userName());

        final var user = userService.createUser(creationRequest.userName());
        final var group = groupService.createGroup(groupName);
        return userGroupService.saveUserAndGroup(user, group);
    }

}
