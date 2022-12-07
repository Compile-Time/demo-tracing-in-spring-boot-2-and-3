package com.example.springboot3server.cmd;

import com.example.commoninterface.filepermission.FilePermission;
import com.example.springboot3server.entity.Group;
import com.example.springboot3server.entity.User;
import lombok.Builder;

@Builder
public record CreateFile(
        String name,
        User user,
        Group group,
        FilePermission userPermissions,
        FilePermission groupPermissions,
        FilePermission otherPermissions
) {
}
