package com.example.sleuthannotationport.data;

import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Data
public class AnnotationJoinPointData<T extends Annotation> {

    private final ProceedingJoinPoint proceedingJoinPoint;
    private final Method targetMethod;
    private final T annotation;

    public AnnotationJoinPointData(final ProceedingJoinPoint proceedingJoinPoint, final Class<T> annotationClass) throws NoSuchMethodException {
        this.targetMethod = getMethod(proceedingJoinPoint, annotationClass);
        this.proceedingJoinPoint = proceedingJoinPoint;
        this.annotation = this.targetMethod.getAnnotation(annotationClass);
    }

    private Method getMethod(final ProceedingJoinPoint pjp, final Class<T> targetAnnotation) throws NoSuchMethodException {
        final Method method = ((MethodSignature) pjp.getSignature()).getMethod();

        if (method.getAnnotation(targetAnnotation) == null) {
            return pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
        }

        return method;
    }
}
