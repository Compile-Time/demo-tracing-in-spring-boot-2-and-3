package com.example.springboot3server.demo.observationdocumentation;

import com.example.commoninterface.file.FileCreationRequest;
import com.example.springboot3server.entity.File;
import com.example.springboot3server.observationconfig.FileCreationObservationContext;
import com.example.springboot3server.observationdocumentation.FileCreationObservationDocumentation;
import com.example.springboot3server.service.FileCreationService;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObservationDocumentFileCreationService {

    private final FileCreationService fileCreationService;
    private final ObservationRegistry observationRegistry;

    public File create(final FileCreationRequest creationRequest) {
        return FileCreationObservationDocumentation.FILE_CREATION
                .observation(observationRegistry, () -> FileCreationObservationContext.from(creationRequest))
                .observe(() -> fileCreationService.create(creationRequest));
    }

}
