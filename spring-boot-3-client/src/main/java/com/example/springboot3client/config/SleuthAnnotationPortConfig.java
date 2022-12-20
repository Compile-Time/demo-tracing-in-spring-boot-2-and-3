package com.example.springboot3client.config;

import com.example.sleuthannotationport.autoconfig.SleuthAnnotationPortAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SleuthAnnotationPortAutoConfiguration.class)
public class SleuthAnnotationPortConfig {
}
