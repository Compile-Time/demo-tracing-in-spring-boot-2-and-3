package com.example.sleuthannotationport.autoconfig;

import com.example.sleuthannotationport.annotationprocessor.NewSpanProcessor;
import io.micrometer.tracing.Tracer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SleuthAnnotationPortAutoconfiguration {

    @Bean
    @ConditionalOnMissingBean
    public NewSpanProcessor newSpanProcessor(final Tracer tracer) {
        return new NewSpanProcessor(tracer);
    }

}
