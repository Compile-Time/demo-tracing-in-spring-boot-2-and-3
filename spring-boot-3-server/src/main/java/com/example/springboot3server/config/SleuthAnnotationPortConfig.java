package com.example.springboot3server.config;

import com.example.sleuthannotationport.NewSpanProcessor;
import com.example.sleuthannotationport.SleuthAnnotationPortAspect;
import com.example.sleuthannotationport.autoconfig.SleuthAnnotationPortAutoconfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(SleuthAnnotationPortAutoconfiguration.class)
public class SleuthAnnotationPortConfig {

    @Bean
    public SleuthAnnotationPortAspect sleuthAnnotationPortAspect(final NewSpanProcessor newSpanProcessor) {
        return new SleuthAnnotationPortAspect(newSpanProcessor);
    }

}
