package com.example.springboot3server.config;

import com.example.springboot3server.observationconfig.FileCreationObservationHandler;
import com.example.springboot3server.observationconfig.FileCreationObservationPredicate;
import com.example.springboot3server.observationconfig.RegionObservationFilter;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ObservationRegistryConfig implements ObservationRegistryCustomizer<ObservationRegistry> {

    @Value("${region}")
    private String region;

    @Override
    public void customize(final ObservationRegistry registry) {
        log.debug("Using following region for observation filter: {}", region);
        registry.observationConfig()
                .observationFilter(new RegionObservationFilter(region))
                .observationPredicate(new FileCreationObservationPredicate())
                .observationHandler(new FileCreationObservationHandler());
    }
}
