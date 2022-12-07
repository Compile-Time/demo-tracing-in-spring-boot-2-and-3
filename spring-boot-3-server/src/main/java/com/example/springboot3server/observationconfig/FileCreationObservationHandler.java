package com.example.springboot3server.observationconfig;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileCreationObservationHandler implements ObservationHandler<FileCreationObservationContext> {

    @Override
    public boolean supportsContext(final Observation.Context context) {
        return context instanceof FileCreationObservationContext;
    }

    @Override
    public void onStart(final FileCreationObservationContext context) {
        log.info("File creation observation started for context: {}", context);
    }

    @Override
    public void onError(final FileCreationObservationContext context) {
        log.error("Error occurred while observing context: {}", context);
    }

    @Override
    public void onStop(final FileCreationObservationContext context) {
        log.info("File creation observation stopped for context: {}", context);
    }
}
