package com.example.springboot3server.demo.observation;

import com.example.commoninterface.file.FileCreationRequest;
import com.example.springboot3server.entity.File;
import com.example.springboot3server.observationconfig.FileCreationObservationContext;
import com.example.springboot3server.service.FileCreationService;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObservationConventionFileCreationService {

    private final FileCreationService fileCreationService;
    private final ObservationRegistry observationRegistry;

    public File create(final FileCreationRequest creationRequest) {
        return Observation.createNotStarted(
                "file.creation",
                () -> FileCreationObservationContext.from(creationRequest),
                observationRegistry
        ).observe(() -> fileCreationService.create(creationRequest));
    }
}
