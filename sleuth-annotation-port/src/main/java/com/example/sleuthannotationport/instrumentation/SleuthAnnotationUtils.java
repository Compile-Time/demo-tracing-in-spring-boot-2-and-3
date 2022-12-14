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

package com.example.sleuthannotationport.instrumentation;

import io.micrometer.tracing.annotation.ContinueSpan;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that can verify whether the method is annotated with the Sleuth
 * annotations.
 *
 * @author Christian Schwerdtfeger
 * @since 1.2.0
 */
final class SleuthAnnotationUtils {

    private static final Log log = LogFactory.getLog(SleuthAnnotationUtils.class);

    private SleuthAnnotationUtils() {

    }

    static boolean isMethodAnnotated(final Method method) {
        return findAnnotation(method, NewSpan.class) != null || findAnnotation(method, ContinueSpan.class) != null;
    }

    static boolean hasAnnotatedParams(final Method method, final Object[] args) {
        return !findAnnotatedParameters(method, args).isEmpty();
    }

    static List<SleuthAnnotatedParameter> findAnnotatedParameters(final Method method, final Object[] args) {
        final Annotation[][] parameters = method.getParameterAnnotations();
        final List<SleuthAnnotatedParameter> result = new ArrayList<>();
        int i = 0;
        for (final Annotation[] parameter : parameters) {
            for (final Annotation parameter2 : parameter) {
                if (parameter2 instanceof SpanTag) {
                    result.add(new SleuthAnnotatedParameter(i, (SpanTag) parameter2, args[i]));
                }
            }
            i++;
        }
        return result;
    }

    /**
     * Searches for an annotation either on a method or inside the method parameters.
     *
     * @param <T>    - annotation
     * @param clazz  - class with annotation
     * @param method - annotated method
     * @return annotation
     */
    static <T extends Annotation> T findAnnotation(final Method method, final Class<T> clazz) {
        T annotation = AnnotationUtils.findAnnotation(method, clazz);
        if (annotation == null) {
            try {
                annotation = AnnotationUtils.findAnnotation(
                        method.getDeclaringClass().getMethod(method.getName(), method.getParameterTypes()), clazz);
            } catch (final NoSuchMethodException | SecurityException ex) {
                if (log.isDebugEnabled()) {
                    log.debug("Exception occurred while tyring to find the annotation", ex);
                }
            }
        }
        return annotation;
    }

}
