package com.example.springboot3server.service;

import com.example.springboot3server.entity.Group;
import com.example.springboot3server.entity.User;
import com.example.springboot3server.entity.UserGroup;
import com.example.springboot3server.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;

    @Transactional
    public UserGroup saveUserAndGroup(final User user, final Group group) {
        return userGroupRepository.save(UserGroup.builder()
                .group(group)
                .user(user)
                .build());
    }
}
