package com.example.sleuthannotationport.autoconfig;

import com.example.sleuthannotationport.SleuthAnnotationPortAspect;
import com.example.sleuthannotationport.annotationprocessor.ContinueSpanProcessor;
import com.example.sleuthannotationport.annotationprocessor.NewSpanProcessor;
import io.micrometer.tracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SleuthAnnotationPortAutoconfiguration {

    @Bean
    public NewSpanProcessor newSpanProcessor(final Tracer tracer) {
        return new NewSpanProcessor(tracer);
    }

    @Bean
    public ContinueSpanProcessor continueSpanProcessor(final Tracer tracer) {
        return new ContinueSpanProcessor(tracer);
    }

    @Bean
    public SleuthAnnotationPortAspect sleuthAnnotationPortAspect(final NewSpanProcessor newSpanProcessor,
                                                                 final ContinueSpanProcessor continueSpanProcessor) {
        return new SleuthAnnotationPortAspect(newSpanProcessor, continueSpanProcessor);
    }

}
