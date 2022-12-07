package com.example.springboot3server.service;

import com.example.springboot3server.entity.Group;
import com.example.springboot3server.repository.GroupRepository;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public Optional<Group> findByGroupName(final String groupName) {
        return groupRepository.findByName(groupName);
    }

}
