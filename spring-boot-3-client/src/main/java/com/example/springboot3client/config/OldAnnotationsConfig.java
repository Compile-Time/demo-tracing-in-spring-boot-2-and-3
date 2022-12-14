package com.example.springboot3client.config;

import com.example.sleuthannotationport.autoconfig.SleuthAnnotationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(SleuthAnnotationConfiguration.class)
@Configuration
public class OldAnnotationsConfig {
}
