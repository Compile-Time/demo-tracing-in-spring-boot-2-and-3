package com.example.springboot3server.config;

import io.micrometer.common.annotation.ValueExpressionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.instrument.annotation.SpelTagValueExpressionResolver;

@RequiredArgsConstructor
public class MicrometerSpelTagValueExpressionResolverWrapper implements ValueExpressionResolver {

    private final SpelTagValueExpressionResolver spelTagValueExpressionResolver;

    @Override
    public String resolve(String expression, Object parameter) {
        return spelTagValueExpressionResolver.resolve(expression, parameter);
    }
}
