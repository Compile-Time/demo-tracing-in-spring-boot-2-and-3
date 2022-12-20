package com.example.sleuthannotationport.util;

import com.example.sleuthannotationport.data.SpanTagData;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpanTagParser {

    public static List<SpanTagData> parseFromJoinPoint(final ProceedingJoinPoint pjp, final Method method) {
        final var spanTagData = new ArrayList<SpanTagData>();
        final var parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                final var annotation = parameterAnnotations[i][j];

                if (annotation instanceof SpanTag atSpanTag) {
                    spanTagData.add(new SpanTagData(atSpanTag, pjp.getArgs()[i]));
                }
            }
        }

        return spanTagData;
    }

}
