package com.example.springboot3server.config;

import com.example.sleuthannotationport.autoconfig.SleuthAnnotationPortAutoconfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(SleuthAnnotationPortAutoconfiguration.class)
public class SleuthAnnotationPortConfig {
}
