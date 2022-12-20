package com.example.sleuthannotationport.annotationprocessor;

import com.example.sleuthannotationport.data.SpanData;
import com.example.sleuthannotationport.data.SpanTagData;
import com.example.sleuthannotationport.util.SpanTagParser;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class NewSpanProcessor {

    private final Tracer tracer;

    public Object createNewSpanFromJoinPoint(final ProceedingJoinPoint pjp,
                                             final Method method,
                                             final NewSpan atNewSpan) throws Throwable {
        final List<SpanTagData> spanTagData = SpanTagParser.parseFromJoinPoint(pjp, method);
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
