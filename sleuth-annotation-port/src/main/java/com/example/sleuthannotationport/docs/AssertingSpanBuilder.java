/*
 * Copyright 2013-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.sleuthannotationport.docs;

import io.micrometer.common.docs.KeyName;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.docs.EventValue;

import java.util.concurrent.TimeUnit;

/**
 * A {@link Span.Builder} that can perform assertions on itself.
 *
 * @author Marcin Grzejszczak
 * @since 3.1.0
 */
public interface AssertingSpanBuilder extends Span.Builder {

    /**
     * @return a {@link DocumentedSpan} with span configuration
     */
    DocumentedSpan getDocumentedSpan();

    /**
     * @return wrapped {@link Span.Builder}
     */
    Span.Builder getDelegate();

    @Override
    default AssertingSpanBuilder tag(final String key, final String value) {
        DocumentedSpanAssertions.assertThatKeyIsValid(key, getDocumentedSpan());
        getDelegate().tag(key, value);
        return this;
    }

    /**
     * Sets a tag on a span.
     *
     * @param key   tag key
     * @param value tag
     * @return this, for chaining
     */
    default AssertingSpanBuilder tag(final KeyName key, final String value) {
        DocumentedSpanAssertions.assertThatKeyIsValid(key, getDocumentedSpan());
        getDelegate().tag(key.asString(), value);
        return this;
    }

    @Override
    default AssertingSpanBuilder event(final String value) {
        DocumentedSpanAssertions.assertThatEventIsValid(value, getDocumentedSpan());
        getDelegate().event(value);
        return this;
    }

    /**
     * Sets an event on a span.
     *
     * @param value event
     * @return this, for chaining
     */
    default AssertingSpanBuilder event(final EventValue value) {
        DocumentedSpanAssertions.assertThatEventIsValid(value, getDocumentedSpan());
        getDelegate().event(value.getValue());
        return this;
    }

    @Override
    default AssertingSpanBuilder name(final String name) {
        DocumentedSpanAssertions.assertThatNameIsValid(name, getDocumentedSpan());
        getDelegate().name(name);
        return this;
    }

    @Override
    default AssertingSpanBuilder error(final Throwable throwable) {
        getDelegate().error(throwable);
        return this;
    }

    @Override
    default AssertingSpanBuilder remoteServiceName(final String remoteServiceName) {
        getDelegate().remoteServiceName(remoteServiceName);
        return this;
    }

    @Override
    default Span.Builder remoteIpAndPort(final String ip, final int port) {
        getDelegate().remoteIpAndPort(ip, port);
        return this;
    }

    @Override
    default AssertingSpanBuilder setParent(final TraceContext context) {
        getDelegate().setParent(context);
        return this;
    }

    @Override
    default AssertingSpanBuilder setNoParent() {
        getDelegate().setNoParent();
        return this;
    }

    @Override
    default AssertingSpanBuilder kind(final Span.Kind spanKind) {
        getDelegate().kind(spanKind);
        return this;
    }

    @Override
    default AssertingSpan start() {
        final Span span = getDelegate().start();
        final DocumentedSpan documentedSpan = getDocumentedSpan();
        return new AssertingSpan() {
            @Override
            public DocumentedSpan getDocumentedSpan() {
                return documentedSpan;
            }

            @Override
            public Span getDelegate() {
                return span;
            }

            @Override
            public boolean isStarted() {
                return true;
            }

            @Override
            public String toString() {
                return getDelegate().toString();
            }
        };
    }

    @Override
    default AssertingSpanBuilder startTimestamp(final long startTimestamp, final TimeUnit unit) {
        final Span.Builder spanBuilder = getDelegate().startTimestamp(startTimestamp, unit);
        final DocumentedSpan documentedSpan = getDocumentedSpan();
        return new AssertingSpanBuilder() {
            @Override
            public DocumentedSpan getDocumentedSpan() {
                return documentedSpan;
            }

            @Override
            public Span.Builder getDelegate() {
                return spanBuilder;
            }
        };
    }

    /**
     * @param documentedSpan span configuration
     * @param builder        builder to wrap in assertions
     * @return asserting span builder
     */
    static AssertingSpanBuilder of(final DocumentedSpan documentedSpan, final Span.Builder builder) {
        if (builder == null) {
            return null;
        } else if (builder instanceof AssertingSpanBuilder) {
            return (AssertingSpanBuilder) builder;
        }
        return new ImmutableAssertingSpanBuilder(documentedSpan, builder);
    }

}
