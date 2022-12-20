package com.example.springboot3server.config;

import com.example.sleuthannotationport.autoconfig.SleuthAnnotationPortAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(SleuthAnnotationPortAutoConfiguration.class)
public class SleuthAnnotationPortConfig {
}
