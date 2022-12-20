package com.example.sleuthannotationport;

import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class NewSpanProcessor {

    private final Tracer tracer;

    public Object createNewSpan(final ProceedingJoinPoint pjp, final Method method, final NewSpan atNewSpan) throws Throwable {
        final List<SpanTagData> spanTagData = parseSpanTags(pjp, method);
        log.debug("SpanTags to tag: {}", spanTagData);
        spanTagData.add(new SpanTagData(SpanData.Tags.CLASS.asString(), method.getDeclaringClass().getName()));
        spanTagData.add(new SpanTagData(SpanData.Tags.METHOD.asString(), method.getName()));

        final var span = this.tracer.nextSpan()
                .name(getSpanName(atNewSpan, method));

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

    private List<SpanTagData> parseSpanTags(final ProceedingJoinPoint pjp, final Method method) {
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

    private String getSpanName(final NewSpan atNewSpan, final Method method) {
        final String name;
        if (atNewSpan.name().isBlank() && atNewSpan.value().isBlank()) {
            name = method.getName();
        } else {
            name = (!atNewSpan.name().isBlank()) ? atNewSpan.name() : atNewSpan.value();
        }

        final var maxLength = Math.min(name.length(), 50);
        return name.toLowerCase().substring(0, maxLength);
    }

}
