package com.example.sleuthannotationport.annotationprocessor;

import com.example.sleuthannotationport.data.AnnotationJoinPointData;
import com.example.sleuthannotationport.data.SpanData;
import com.example.sleuthannotationport.data.SpanTagData;
import com.example.sleuthannotationport.util.SpanTagParser;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.List;

/**
 * This class processes a {@link ProceedingJoinPoint} for {@link NewSpan}.
 * <p>
 * The implementation present in this class might not be as complete as the implementation in Spring Cloud Sleuth and
 * does not provide any guarantees about being on par with the one in Sleuth. It exists mostly for demo purposes and to
 * assess the difficulty creating an aspect for Sleuth's old annotations with Micrometer Tracing's APIs.
 */
@RequiredArgsConstructor
public class NewSpanProcessor {

    private final Tracer tracer;

    public Object createNewSpanFromJoinPoint(final AnnotationJoinPointData<NewSpan> joinPointData) throws Throwable {
        final var pjp = joinPointData.getProceedingJoinPoint();
        final var atNewSpan = joinPointData.getAnnotation();
        final var targetMethod = joinPointData.getTargetMethod();

        final List<SpanTagData> spanTagData = SpanTagParser.parseFromJoinPoint(joinPointData);
        spanTagData.add(new SpanTagData(SpanData.Tags.CLASS.asString(), targetMethod.getDeclaringClass().getName()));
        spanTagData.add(new SpanTagData(SpanData.Tags.METHOD.asString(), targetMethod.getName()));

        final var span = this.tracer.nextSpan()
                .name(getSpanName(atNewSpan, targetMethod));

        try (final var ignore = this.tracer.withSpan(span.start())) {
            spanTagData.forEach(tagData -> span.tag(tagData.getTagName(), tagData.getTagValue()));

            return pjp.proceed();
        } catch (final Throwable e) {
            span.error(e);
            throw e;
        } finally {
            span.end();
        }
    }

    private String getSpanName(final NewSpan atNewSpan, final Method targetMethod) {
        final String name;
        if (atNewSpan.name().isBlank() && atNewSpan.value().isBlank()) {
            name = targetMethod.getName();
        } else {
            name = (!atNewSpan.name().isBlank()) ? atNewSpan.name() : atNewSpan.value();
        }

        final var maxLength = Math.min(name.length(), 50);
        return name.toLowerCase().substring(0, maxLength);
    }

}
