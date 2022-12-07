package com.example.springboot3server.observationscope;

import com.example.commoninterface.file.FileCreationRequest;
import com.example.springboot3server.entity.File;
import com.example.springboot3server.service.FileCreationService;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObservationScopeFileCreationService {

    private final FileCreationService fileCreationService;
    private final ObservationRegistry observationRegistry;

    public File create(final FileCreationRequest creationRequest) {
        final var observation = Observation.start("file.create", observationRegistry);

        try (final var ignored = observation.openScope()) {
            observation.contextualName(
                    String.format("create new file for user '%s' and group ''%s (Observation Scope)",
                            creationRequest.userName(), creationRequest.groupName()));
            observation.lowCardinalityKeyValue("file.user.name", creationRequest.userName());
            observation.lowCardinalityKeyValue("file.group.name", creationRequest.groupName());
            observation.highCardinalityKeyValue("file.name", creationRequest.fileName());

            observation.event(Observation.Event.of("start file creation"));
            return fileCreationService.create(creationRequest);
        } finally {
            observation.event(Observation.Event.of("end file creation"));
            observation.stop();
        }
    }

}
