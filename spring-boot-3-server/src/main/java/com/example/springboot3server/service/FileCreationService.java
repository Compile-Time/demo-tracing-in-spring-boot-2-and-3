package com.example.springboot3server.service;

import com.example.commoninterface.file.FileCreationRequest;
import com.example.springboot3server.cmd.CreateFile;
import com.example.springboot3server.entity.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileCreationService {

    private final UserService userService;
    private final GroupService groupService;
    private final FileService fileService;

    @Transactional
    public File create(final FileCreationRequest creationRequest) {
        final var user = userService.findByUserName(creationRequest.userName()).orElseThrow(() ->
                new RuntimeException(
                        String.format("No user with the name %s found", creationRequest.userName())
                )
        );
        final var group = groupService.findByGroupName(creationRequest.groupName()).orElseThrow(() ->
                new RuntimeException(
                        String.format("No group with the name %s found", creationRequest.userName())
                )
        );
        return fileService.createFile(CreateFile.builder()
                .name(creationRequest.fileName())
                .user(user)
                .group(group)
                .userPermissions(creationRequest.getUserFilePermission())
                .groupPermissions(creationRequest.getGroupFilePermission())
                .otherPermissions(creationRequest.getOtherFilePermission())
                .build());
    }
}
