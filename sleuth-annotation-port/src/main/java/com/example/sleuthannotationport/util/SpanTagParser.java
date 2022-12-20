package com.example.sleuthannotationport.util;

import com.example.sleuthannotationport.data.AnnotationJoinPointData;
import com.example.sleuthannotationport.data.SpanTagData;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * This class processes a {@link ProceedingJoinPoint} to extract data from {@link SpanTag}s.
 * <p>
 * The implementation present in this class might not be as complete as the implementation in Spring Cloud Sleuth and
 * does not provide any guarantees about being on par with the one in Sleuth. It exists mostly for demo purposes and to
 * assess the difficulty creating an aspect for Sleuth's old annotations with Micrometer Tracing's APIs.
 */
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
