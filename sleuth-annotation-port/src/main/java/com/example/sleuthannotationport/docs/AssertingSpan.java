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
 * {@link Span} that performs additional assertions such as allowed name, tag, event
 * verification and upon reporting, whether the span had been started in the first place.
 * <p>
 * You need to turn on assertions via system properties or environment variables to start
 * breaking your tests or production code. Check {@link DocumentedSpanAssertions} for more
 * information.
 *
 * @author Marcin Grzejszczak
 * @since 3.1.0
 */
public interface AssertingSpan extends Span {

    /**
     * @return a {@link DocumentedSpan} with span configuration
     */
    DocumentedSpan getDocumentedSpan();

    /**
     * @return wrapped {@link Span}
     */
    Span getDelegate();

    /**
     * @return {@code true} when this span was started
     */
    default boolean isStarted() {
        return false;
    }

    @Override
    default AssertingSpan tag(final String key, final String value) {
        DocumentedSpanAssertions.assertThatKeyIsValid(key, getDocumentedSpan());
        getDelegate().tag(key, value);
        return this;
    }

    /**
     * Tags a span via {@link KeyName}.
     *
     * @param key   tag key
     * @param value tag value
     * @return this for chaining
     */
    default AssertingSpan tag(final KeyName key, final String value) {
        DocumentedSpanAssertions.assertThatKeyIsValid(key, getDocumentedSpan());
        getDelegate().tag(key.asString(), value);
        return this;
    }

    @Override
    default AssertingSpan event(final String value) {
        DocumentedSpanAssertions.assertThatEventIsValid(value, getDocumentedSpan());
        getDelegate().event(value);
        return this;
    }

    /**
     * Annotates with an event via {@link EventValue}.
     *
     * @param value event value
     * @return this for chaining
     */
    default AssertingSpan event(final EventValue value) {
        DocumentedSpanAssertions.assertThatEventIsValid(value, getDocumentedSpan());
        getDelegate().event(value.getValue());
        return this;
    }

    @Override
    default AssertingSpan event(final String value, final long time, final TimeUnit timeUnit) {
        DocumentedSpanAssertions.assertThatEventIsValid(value, getDocumentedSpan());
        getDelegate().event(value, time, timeUnit);
        return this;
    }

    @Override
    default AssertingSpan name(final String name) {
        DocumentedSpanAssertions.assertThatNameIsValid(name, getDocumentedSpan());
        getDelegate().name(name);
        return this;
    }

    @Override
    default boolean isNoop() {
        return getDelegate().isNoop();
    }

    @Override
    default TraceContext context() {
        return getDelegate().context();
    }

    @Override
    default AssertingSpan start() {
        getDelegate().start();
        return this;
    }

    @Override
    default AssertingSpan error(final Throwable throwable) {
        getDelegate().error(throwable);
        return this;
    }

    @Override
    default void end() {
        DocumentedSpanAssertions.assertThatSpanStartedBeforeEnd(this);
        getDelegate().end();
    }

    @Override
    default void end(final long time, final TimeUnit timeUnit) {
        DocumentedSpanAssertions.assertThatSpanStartedBeforeEnd(this);
        getDelegate().end(time, timeUnit);
    }

    @Override
    default void abandon() {
        getDelegate().abandon();
    }

    @Override
    default AssertingSpan remoteServiceName(final String remoteServiceName) {
        getDelegate().remoteServiceName(remoteServiceName);
        return this;
    }

    @Override
    default Span remoteIpAndPort(final String ip, final int port) {
        getDelegate().remoteIpAndPort(ip, port);
        return this;
    }

    /**
     * @param documentedSpan span configuration
     * @param span           span to wrap in assertions
     * @return asserting span
     */
    static AssertingSpan of(final DocumentedSpan documentedSpan, final Span span) {
        if (span == null) {
            return null;
        } else if (span instanceof AssertingSpan) {
            return (AssertingSpan) span;
        }
        return new ImmutableAssertingSpan(documentedSpan, span);
    }

    /**
     * @param documentedSpan span configuration
     * @param span           span to wrap in assertions
     * @return asserting span
     */
    static AssertingSpan continueSpan(final DocumentedSpan documentedSpan, final Span span) {
        final AssertingSpan assertingSpan = of(documentedSpan, span);
        if (assertingSpan == null) {
            return null;
        }
        ((ImmutableAssertingSpan) assertingSpan).isStarted = true;
        return assertingSpan;
    }

    /**
     * Returns the underlying delegate. Used when casting is necessary.
     *
     * @param span span to check for wrapping
     * @param <T>  type extending a span
     * @return unwrapped object
     */
    static <T extends Span> T unwrap(final Span span) {
        if (span == null) {
            return null;
        } else if (span instanceof AssertingSpan) {
            return (T) ((AssertingSpan) span).getDelegate();
        }
        return (T) span;
    }

}
