package com.example.springboot3server.observationconfig;

import com.example.commoninterface.file.FileCreationRequest;
import io.micrometer.observation.Observation;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Getter
public class FileCreationObservationContext extends Observation.Context {
    private final String fileName;
    private final String userName;
    private final String groupName;
    private final String userPermissions;
    private final String groupPermissions;
    private final String otherPermissions;

    public static FileCreationObservationContext from(final FileCreationRequest creationRequest) {
        return FileCreationObservationContext.builder()
                .fileName(creationRequest.fileName())
                .userName(creationRequest.userName())
                .groupName(creationRequest.groupName())
                .userPermissions(creationRequest.userPermissions())
                .groupPermissions(creationRequest.groupPermissions())
                .otherPermissions(creationRequest.otherPermissions())
                .build();
    }
}
