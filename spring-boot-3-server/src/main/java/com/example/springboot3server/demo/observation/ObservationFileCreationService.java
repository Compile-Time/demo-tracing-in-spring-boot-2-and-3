package com.example.springboot3server.demo.observation;

import com.example.commoninterface.file.FileCreationRequest;
import com.example.springboot3server.entity.File;
import com.example.springboot3server.service.FileCreationService;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObservationFileCreationService {

    private final FileCreationService fileCreationService;
    private final ObservationRegistry observationRegistry;

    public File create(final FileCreationRequest creationRequest) {
        return Observation.createNotStarted("file.create", observationRegistry)
                .contextualName(String.format("create file for user '%s' and group '%s' (Observation API)",
                        creationRequest.userName(), creationRequest.groupName()))
                .lowCardinalityKeyValue("file.user.name", creationRequest.userName())
                .lowCardinalityKeyValue("file.group.name", creationRequest.groupName())
                .highCardinalityKeyValue("file.name", creationRequest.fileName())
                .observe(() -> fileCreationService.create(creationRequest));
    }

}
