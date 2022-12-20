package com.example.springboot3client.config;

import com.example.sleuthannotationport.autoconfig.SleuthAnnotationPortAutoconfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SleuthAnnotationPortAutoconfiguration.class)
public class SleuthAnnotationPortConfig {
}
