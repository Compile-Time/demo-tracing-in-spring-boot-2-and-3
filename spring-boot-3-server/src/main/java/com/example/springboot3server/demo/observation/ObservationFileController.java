package com.example.springboot3server.demo.observation;

import com.example.commoninterface.file.FileCreationRequest;
import com.example.commoninterface.file.FileCreationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/observation/files")
@RequiredArgsConstructor
public class ObservationFileController {

    private final ObservationFileCreationService observationFileCreationService;

    @PostMapping
    public ResponseEntity<FileCreationResponse> postFile(@RequestBody final FileCreationRequest creationRequest) {
        final var file = observationFileCreationService.create(creationRequest);
        return ResponseEntity.ok(FileCreationResponse.builder()
                .fileName(file.getName())
                .userName(file.getUser().getName())
                .groupName(file.getGroup().getName())
                .userPermissions(file.getUserPermission().getPermissionString())
                .groupPermissions(file.getGroupPermission().getPermissionString())
                .otherPermissions(file.getOtherPermission().getPermissionString())
                .build());
    }

}
