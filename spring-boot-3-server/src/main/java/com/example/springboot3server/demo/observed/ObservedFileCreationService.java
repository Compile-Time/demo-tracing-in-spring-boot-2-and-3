package com.example.springboot3server.demo.observed;

import com.example.commoninterface.file.FileCreationRequest;
import com.example.springboot3server.entity.File;
import com.example.springboot3server.service.FileCreationService;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObservedFileCreationService {

    private final FileCreationService fileCreationService;

    @Observed(
            name = "file.creation",
            contextualName = "observed annotation showcase",
            lowCardinalityKeyValues = {"class.name", "ObservedFileCreationService"}
    )
    public File create(final FileCreationRequest creationRequest) {
        return fileCreationService.create(creationRequest);
    }

}
