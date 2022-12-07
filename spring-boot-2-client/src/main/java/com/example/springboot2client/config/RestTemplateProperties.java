package com.example.springboot2client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest-client")
@Getter
@Setter
public class RestTemplateProperties {

    private String host;
    private String port;

    public String getUrl() {
        return String.format("%s:%s", host, port);
    }

}
