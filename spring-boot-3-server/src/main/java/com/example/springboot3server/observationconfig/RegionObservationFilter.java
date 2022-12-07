package com.example.springboot3server.observationconfig;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegionObservationFilter implements ObservationFilter {

    private final String region;

    @Override
    public Observation.Context map(final Observation.Context context) {
        return context
                .put("region", region)
                .addLowCardinalityKeyValue(KeyValue.of("region", region));
    }
}
