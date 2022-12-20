package com.example.sleuthannotationport.annotationprocessor;

import com.example.sleuthannotationport.data.AnnotationJoinPointData;
import com.example.sleuthannotationport.data.SpanData;
import com.example.sleuthannotationport.data.SpanTagData;
import com.example.sleuthannotationport.util.SpanTagParser;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.ContinueSpan;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

@RequiredArgsConstructor
public class ContinueSpanProcessor {

    private final Tracer tracer;

    public Object modifyExistingSpanFromJoinPoint(final AnnotationJoinPointData<ContinueSpan> joinPointData) throws Throwable {
        final var pjp = joinPointData.getProceedingJoinPoint();
        final var atContinueSpan = joinPointData.getAnnotation();

        final var span = this.tracer.currentSpan();
        if (span == null) {
            return pjp.proceed();
        }
        final List<SpanTagData> spanTagData = SpanTagParser.parseFromJoinPoint(joinPointData);
        spanTagData.forEach(tagData -> span.tag(tagData.getTagName(), tagData.getTagValue()));

        try {
            triggerLogEventIfRelevant(span, atContinueSpan, SpanData.Events.BEFORE.asString());
            return pjp.proceed();
        } catch (final Exception e) {
            span.error(e);
            throw e;
        } finally {
            triggerLogEventIfRelevant(span, atContinueSpan, SpanData.Events.AFTER.asString());
        }
    }

    private void triggerLogEventIfRelevant(final Span span, final ContinueSpan atContinueSpan, final String eventText) {
        final var logText = atContinueSpan.log();
        if (!logText.isBlank()) {
            span.event(String.format(eventText, logText));
        }
    }

}
