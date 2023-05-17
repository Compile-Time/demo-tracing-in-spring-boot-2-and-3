package com.example.springboot3server.config;

import io.micrometer.common.annotation.NoOpValueResolver;
import io.micrometer.common.annotation.ValueExpressionResolver;
import io.micrometer.common.annotation.ValueResolver;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.annotation.SpelTagValueExpressionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SpanAspectConfig {

    @Bean
    public NewSpanParser newSpanParser() {
        return new DefaultNewSpanParser();
    }

    // You can provide your own resolvers - here we go with a noop example.
    @Bean
    public ValueResolver valueResolver() {
        return new NoOpValueResolver();
    }

    // Example of a SpEL resolver
    @Bean
    public ValueExpressionResolver valueExpressionResolver() {
        return new MicrometerSpelTagValueExpressionResolverWrapper(new SpelTagValueExpressionResolver());
    }

    @Bean
    public MethodInvocationProcessor methodInvocationProcessor(NewSpanParser newSpanParser, Tracer tracer,
                                                        BeanFactory beanFactory) {
        return new ImperativeMethodInvocationProcessor(newSpanParser, tracer, beanFactory::getBean,
                beanFactory::getBean);
    }

    @Bean
    public SpanAspect spanAspect(MethodInvocationProcessor methodInvocationProcessor) {
        return new SpanAspect(methodInvocationProcessor);
    }

}
