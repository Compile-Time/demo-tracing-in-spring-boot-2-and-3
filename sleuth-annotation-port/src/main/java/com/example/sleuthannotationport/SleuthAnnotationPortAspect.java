package com.example.sleuthannotationport;

import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
@RequiredArgsConstructor
public class SleuthAnnotationPortAspect {

    private final NewSpanProcessor newSpanProcessor;

    @Around("execution(@io.micrometer.tracing.annotation.NewSpan * *(..))")
    public Object processNewSpan(final ProceedingJoinPoint pjp) throws Throwable {
        final var method = getMethod(pjp);
        final var atNewSpan = method.getAnnotation(NewSpan.class);
        return newSpanProcessor.createNewSpan(pjp, method, atNewSpan);
    }

    private Method getMethod(final ProceedingJoinPoint pjp) throws NoSuchMethodException {
        final Method method = ((MethodSignature) pjp.getSignature()).getMethod();

        if (method.getAnnotation(NewSpan.class) == null) {
            return pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
        }

        return method;
    }

}
