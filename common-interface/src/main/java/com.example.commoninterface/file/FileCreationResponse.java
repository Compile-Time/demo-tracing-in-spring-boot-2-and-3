package com.example.commoninterface.file;

import lombok.Builder;

@Builder
public record FileCreationResponse(
        String fileName,
        String userName,
        String userPermissions,
        String groupName,
        String groupPermissions,
        String otherPermissions
) {
}
