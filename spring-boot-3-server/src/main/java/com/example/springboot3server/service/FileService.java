package com.example.springboot3server.service;

import com.example.springboot3server.cmd.CreateFile;
import com.example.springboot3server.entity.File;
import com.example.springboot3server.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public File createFile(final CreateFile createFileCmd) {
        final var file = File.builder()
                .name(createFileCmd.name())
                .user(createFileCmd.user())
                .group(createFileCmd.group())
                .userPermission(createFileCmd.userPermissions())
                .groupPermission(createFileCmd.groupPermissions())
                .otherPermission(createFileCmd.otherPermissions())
                .build();

        return fileRepository.save(file);
    }
}
