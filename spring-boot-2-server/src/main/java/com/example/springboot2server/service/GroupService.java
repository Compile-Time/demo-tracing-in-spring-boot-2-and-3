package com.example.springboot2server.service;

import com.example.springboot2server.entity.Group;
import com.example.springboot2server.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    @Transactional
    public Group createGroup(@SpanTag("group.name") final String groupName) {
        return groupRepository.save(Group.builder()
                .name(groupName)
                .build());
    }

}
