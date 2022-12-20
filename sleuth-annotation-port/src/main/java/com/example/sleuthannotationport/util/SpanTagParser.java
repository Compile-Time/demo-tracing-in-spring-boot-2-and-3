package com.example.sleuthannotationport.util;

import com.example.sleuthannotationport.data.AnnotationJoinPointData;
import com.example.sleuthannotationport.data.SpanTagData;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpanTagParser {

    public static <T extends Annotation> List<SpanTagData> parseFromJoinPoint(final AnnotationJoinPointData<T> joinPointData) {
        final var spanTagData = new ArrayList<SpanTagData>();
        final var parameterAnnotations = joinPointData.getTargetMethod().getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                final var annotation = parameterAnnotations[i][j];

                if (annotation instanceof SpanTag atSpanTag) {
                    spanTagData.add(new SpanTagData(atSpanTag, joinPointData.getProceedingJoinPoint().getArgs()[i]));
                }
            }
        }

        return spanTagData;
    }

}
