package com.example.springboot3server.observationconfig;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationPredicate;

public class FileCreationObservationPredicate implements ObservationPredicate {

    @Override
    public boolean test(final String ignoredObservationName, final Observation.Context context) {
        if (context instanceof FileCreationObservationContext fileCreationObservationContext) {
            return !"logger".equalsIgnoreCase(fileCreationObservationContext.getUserName());
        }
        return true;
    }

}
