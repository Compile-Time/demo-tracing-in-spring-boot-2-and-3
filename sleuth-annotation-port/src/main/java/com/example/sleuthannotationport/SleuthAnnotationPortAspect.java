package com.example.sleuthannotationport;

import com.example.sleuthannotationport.annotationprocessor.ContinueSpanProcessor;
import com.example.sleuthannotationport.annotationprocessor.NewSpanProcessor;
import com.example.sleuthannotationport.data.AnnotationJoinPointData;
import io.micrometer.tracing.annotation.ContinueSpan;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@RequiredArgsConstructor
public class SleuthAnnotationPortAspect {

    private final NewSpanProcessor newSpanProcessor;
    private final ContinueSpanProcessor continueSpanProcessor;

    @Around("execution(@io.micrometer.tracing.annotation.NewSpan * *(..))")
    public Object processNewSpan(final ProceedingJoinPoint pjp) throws Throwable {
        return newSpanProcessor.createNewSpanFromJoinPoint(new AnnotationJoinPointData<>(pjp, NewSpan.class));
    }

    @Around("execution(@io.micrometer.tracing.annotation.ContinueSpan * *(..))")
    public Object processContinueSpan(final ProceedingJoinPoint pjp) throws Throwable {
        return continueSpanProcessor.modifyExistingSpanFromJoinPoint(new AnnotationJoinPointData<>(pjp, ContinueSpan.class));
    }


}
