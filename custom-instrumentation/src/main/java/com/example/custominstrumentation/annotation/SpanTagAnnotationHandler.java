/*
 * Copyright 2013-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.custominstrumentation.annotation;

import io.micrometer.tracing.SpanCustomizer;
import io.micrometer.tracing.annotation.NoOpTagValueResolver;
import io.micrometer.tracing.annotation.SpanTag;
import io.micrometer.tracing.annotation.TagValueExpressionResolver;
import io.micrometer.tracing.annotation.TagValueResolver;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * This class is able to find all methods annotated with the Sleuth annotations. All
 * methods mean that if you have both an interface and an implementation annotated with
 * Sleuth annotations then this class is capable of finding both of them and merging into
 * one set of tracing information.
 * <p>
 * This information is then used to add proper tags to the span from the method arguments
 * that are annotated with {@link SpanTag}.
 *
 * @author Christian Schwerdtfeger
 * @since 1.2.0
 */
class SpanTagAnnotationHandler {

    private static final Log log = LogFactory.getLog(SpanTagAnnotationHandler.class);

    private final BeanFactory beanFactory;

    private SpanCustomizer spanCustomizer;

    SpanTagAnnotationHandler(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    void addAnnotatedParameters(final MethodInvocation pjp) {
        try {
            final Method method = pjp.getMethod();
            final Method mostSpecificMethod = AopUtils.getMostSpecificMethod(method, pjp.getThis().getClass());
            final List<SleuthAnnotatedParameter> annotatedParameters = SleuthAnnotationUtils
                    .findAnnotatedParameters(mostSpecificMethod, pjp.getArguments());
            getAnnotationsFromInterfaces(pjp, mostSpecificMethod, annotatedParameters);
            mergeAnnotatedMethodsIfNecessary(pjp, method, mostSpecificMethod, annotatedParameters);
            addAnnotatedArguments(annotatedParameters);
        } catch (final SecurityException ex) {
            log.error("Exception occurred while trying to add annotated parameters", ex);
        }
    }

    private void getAnnotationsFromInterfaces(final MethodInvocation pjp, final Method mostSpecificMethod,
                                              final List<SleuthAnnotatedParameter> annotatedParameters) {
        final Class<?>[] implementedInterfaces = pjp.getThis().getClass().getInterfaces();
        if (implementedInterfaces.length > 0) {
            for (final Class<?> implementedInterface : implementedInterfaces) {
                for (final Method methodFromInterface : implementedInterface.getMethods()) {
                    if (methodsAreTheSame(mostSpecificMethod, methodFromInterface)) {
                        final List<SleuthAnnotatedParameter> annotatedParametersForActualMethod = SleuthAnnotationUtils
                                .findAnnotatedParameters(methodFromInterface, pjp.getArguments());
                        mergeAnnotatedParameters(annotatedParameters, annotatedParametersForActualMethod);
                    }
                }
            }
        }
    }

    private boolean methodsAreTheSame(final Method mostSpecificMethod, final Method method1) {
        return method1.getName().equals(mostSpecificMethod.getName())
                && Arrays.equals(method1.getParameterTypes(), mostSpecificMethod.getParameterTypes());
    }

    private void mergeAnnotatedMethodsIfNecessary(final MethodInvocation pjp, final Method method, final Method mostSpecificMethod,
                                                  final List<SleuthAnnotatedParameter> annotatedParameters) {
        // that can happen if we have an abstraction and a concrete class that is
        // annotated with @NewSpan annotation
        if (!method.equals(mostSpecificMethod)) {
            final List<SleuthAnnotatedParameter> annotatedParametersForActualMethod = SleuthAnnotationUtils
                    .findAnnotatedParameters(method, pjp.getArguments());
            mergeAnnotatedParameters(annotatedParameters, annotatedParametersForActualMethod);
        }
    }

    private void mergeAnnotatedParameters(final List<SleuthAnnotatedParameter> annotatedParametersIndices,
                                          final List<SleuthAnnotatedParameter> annotatedParametersIndicesForActualMethod) {
        for (final SleuthAnnotatedParameter container : annotatedParametersIndicesForActualMethod) {
            final int index = container.parameterIndex;
            boolean parameterContained = false;
            for (final SleuthAnnotatedParameter parameterContainer : annotatedParametersIndices) {
                if (parameterContainer.parameterIndex == index) {
                    parameterContained = true;
                    break;
                }
            }
            if (!parameterContained) {
                annotatedParametersIndices.add(container);
            }
        }
    }

    private void addAnnotatedArguments(final List<SleuthAnnotatedParameter> toBeAdded) {
        for (final SleuthAnnotatedParameter container : toBeAdded) {
            final String tagValue = resolveTagValue(container.annotation, container.argument);
            final String tagKey = resolveTagKey(container);
            span().tag(tagKey, tagValue);
        }
    }

    private SpanCustomizer span() {
        if (this.spanCustomizer == null) {
            this.spanCustomizer = this.beanFactory.getBean(SpanCustomizer.class);
        }
        return this.spanCustomizer;
    }

    private String resolveTagKey(final SleuthAnnotatedParameter container) {
        return StringUtils.hasText(container.annotation.value()) ? container.annotation.value()
                : container.annotation.key();
    }

    String resolveTagValue(final SpanTag annotation, final Object argument) {
        String value = null;
        if (annotation.resolver() != NoOpTagValueResolver.class) {
            final TagValueResolver tagValueResolver = this.beanFactory.getBean(annotation.resolver());
            value = tagValueResolver.resolve(argument);
        } else if (StringUtils.hasText(annotation.expression())) {
            value = this.beanFactory.getBean(TagValueExpressionResolver.class).resolve(annotation.expression(),
                    argument);
        } else if (argument != null) {
            value = argument.toString();
        }
        return value == null ? "" : value;
    }

}
