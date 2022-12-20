package com.example.sleuthannotationport;

import com.example.sleuthannotationport.annotationprocessor.ContinueSpanProcessor;
import com.example.sleuthannotationport.annotationprocessor.NewSpanProcessor;
import io.micrometer.tracing.annotation.ContinueSpan;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@RequiredArgsConstructor
public class SleuthAnnotationPortAspect {

    private final NewSpanProcessor newSpanProcessor;
    private final ContinueSpanProcessor continueSpanProcessor;

    @Around("execution(@io.micrometer.tracing.annotation.NewSpan * *(..))")
    public Object processNewSpan(final ProceedingJoinPoint pjp) throws Throwable {
        final var method = getMethod(pjp, NewSpan.class);
        final var atNewSpan = method.getAnnotation(NewSpan.class);
        return newSpanProcessor.createNewSpanFromJoinPoint(pjp, method, atNewSpan);
    }

    @Around("execution(@io.micrometer.tracing.annotation.ContinueSpan * *(..))")
    public Object processContinueSpan(final ProceedingJoinPoint pjp) throws Throwable {
        final var method = getMethod(pjp, ContinueSpan.class);
        final var atContinueSpan = method.getAnnotation(ContinueSpan.class);
        return continueSpanProcessor.modifyExistingSpanFromJoinPoint(pjp, method, atContinueSpan);
    }

    private <T extends Annotation> Method getMethod(final ProceedingJoinPoint pjp, final Class<T> targetAnnotation) throws NoSuchMethodException {
        final Method method = ((MethodSignature) pjp.getSignature()).getMethod();

        if (method.getAnnotation(targetAnnotation) == null) {
            return pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
        }

        return method;
    }

}
