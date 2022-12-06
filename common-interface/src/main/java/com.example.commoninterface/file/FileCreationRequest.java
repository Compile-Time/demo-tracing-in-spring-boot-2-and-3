package com.example.commoninterface.file;

import com.example.commoninterface.filepermission.FilePermission;
import lombok.Builder;

@Builder
public record FileCreationRequest(
        String fileName,
        String userName,
        String groupName,
        String userPermissions,
        String groupPermissions,
        String otherPermissions
) {

    public FilePermission getUserFilePermission() {
        return FilePermission.from(this.userPermissions);
    }

    public FilePermission getGroupFilePermission() {
        return FilePermission.from(this.groupPermissions);
    }

    public FilePermission getOtherFilePermission() {
        return FilePermission.from(this.otherPermissions);
    }

}
