package com.example.springboot3server.demo.observation;

import com.example.commoninterface.file.FileCreationRequest;
import com.example.commoninterface.file.FileCreationResponse;
import com.example.springboot3server.entity.File;
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
    private final ObservationConventionFileCreationService observationConventionFileCreationService;
    private final ObservationCustomConventionFileCreationService observationCustomConventionFileCreationService;

    private ResponseEntity<FileCreationResponse> createResponse(final File file) {
        return ResponseEntity.ok(FileCreationResponse.builder()
                .fileName(file.getName())
                .userName(file.getUser().getName())
                .groupName(file.getGroup().getName())
                .userPermissions(file.getUserPermission().getPermissionString())
                .groupPermissions(file.getGroupPermission().getPermissionString())
                .otherPermissions(file.getOtherPermission().getPermissionString())
                .build());
    }

    @PostMapping
    public ResponseEntity<FileCreationResponse> postFile(@RequestBody final FileCreationRequest creationRequest) {
        return createResponse(observationFileCreationService.create(creationRequest));
    }

    @PostMapping("/convention")
    public ResponseEntity<FileCreationResponse> postFileConvention(@RequestBody final FileCreationRequest creationRequest) {
        return createResponse(observationConventionFileCreationService.create(creationRequest));
    }

    @PostMapping("/custom-convention")
    public ResponseEntity<FileCreationResponse> postFileOverrideConvention(@RequestBody final FileCreationRequest creationRequest) {
        return createResponse(observationCustomConventionFileCreationService.create(creationRequest));
    }

}
