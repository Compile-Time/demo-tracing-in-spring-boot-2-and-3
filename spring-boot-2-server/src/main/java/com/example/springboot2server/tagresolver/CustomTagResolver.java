package com.example.springboot2server.tagresolver;

import com.example.commoninterface.user.UserCreationRequest;
import org.springframework.cloud.sleuth.annotation.TagValueResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CustomTagResolver {

    @Bean
    public TagValueResolver userCreationRequestTagResolver() {
        return parameter -> {
            if (parameter instanceof final UserCreationRequest creationRequest) {
                return String.format("u:%s - g:%s", creationRequest.userName(),
                        creationRequest.groupName());
            }
            return null;
        };
    }

}