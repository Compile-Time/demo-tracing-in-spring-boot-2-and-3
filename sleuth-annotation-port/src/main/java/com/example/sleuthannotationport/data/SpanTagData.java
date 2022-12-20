package com.example.sleuthannotationport.data;

import io.micrometer.tracing.annotation.SpanTag;
import lombok.Data;

@Data
public class SpanTagData {

    private final String tagName;
    private final String tagValue;

    public SpanTagData(final String tagName, final String tagValue) {
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    public SpanTagData(final SpanTag atSpanTag, final Object parameterValue) {
        this.tagName = determineTagName(atSpanTag);
        this.tagValue = parameterValue.toString();
    }

    private String determineTagName(final SpanTag atSpanTag) {
        return (atSpanTag.key().isBlank()) ? atSpanTag.value() : atSpanTag.key();
    }
}
