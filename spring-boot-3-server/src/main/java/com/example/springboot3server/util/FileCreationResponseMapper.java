package com.example.springboot3server.util;

import com.example.commoninterface.file.FileCreationResponse;
import com.example.springboot3server.entity.File;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileCreationResponseMapper {

    public static FileCreationResponse map(final File file) {
        return FileCreationResponse.builder()
                .fileName(file.getName())
                .userName(file.getUser().getName())
                .groupName(file.getGroup().getName())
                .userPermissions(file.getUserPermission().getPermissionString())
                .groupPermissions(file.getGroupPermission().getPermissionString())
                .otherPermissions(file.getOtherPermission().getPermissionString())
                .build();
    }

}
