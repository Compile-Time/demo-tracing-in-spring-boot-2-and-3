package com.example.springboot3client.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ObservedAspectConfig {

    @Bean
    public ObservedAspect observedAspect(final ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }

}
