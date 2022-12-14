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

package com.example.sleuthannotationport.instrumentation;

import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.ContinueSpan;
import io.micrometer.tracing.annotation.MethodInvocationProcessor;
import io.micrometer.tracing.annotation.NewSpanParser;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Sleuth annotation processor.
 *
 * @author Marcin Grzejszczak
 */
abstract class AbstractMicrometerMethodInvocationProcessor implements MethodInvocationProcessor, BeanFactoryAware {

	private static final Log logger = LogFactory.getLog(AbstractMicrometerMethodInvocationProcessor.class);

	BeanFactory beanFactory;

	private NewSpanParser newSpanParser;

	private Tracer tracer;

	private CurrentTraceContext currentTraceContext;

	private SpanTagAnnotationHandler spanTagAnnotationHandler;

	void before(final MethodInvocation invocation, final Span span, final String log, final boolean hasLog) {
		if (hasLog) {
			logEvent(span, log + ".before");
		}
		spanTagAnnotationHandler().addAnnotatedParameters(invocation);
		addTags(invocation, span);
	}

	void after(final Span span, final boolean isNewSpan, final String log, final boolean hasLog) {
		if (hasLog) {
			logEvent(span, log + ".after");
		}
		if (isNewSpan) {
			span.end();
		}
	}

	void onFailure(final Span span, final String log, final boolean hasLog, final Throwable e) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exception occurred while trying to continue the pointcut", e);
		}
		if (hasLog) {
			logEvent(span, log + ".afterFailure");
		}
		span.error(e);
	}

	void addTags(final MethodInvocation invocation, final Span span) {
		SleuthAnnotationSpan.ANNOTATION_NEW_OR_CONTINUE_SPAN.wrap(span)
				.tag(SleuthAnnotationSpan.Tags.CLASS, invocation.getThis().getClass().getSimpleName())
				.tag(SleuthAnnotationSpan.Tags.METHOD, invocation.getMethod().getName());
	}

	void logEvent(final Span span, final String name) {
		if (span == null) {
			logger.warn("You were trying to continue a span which was null. Please "
					+ "remember that if two proxied methods are calling each other from "
					+ "the same class then the aspect will not be properly resolved");
			return;
		}
		SleuthAnnotationSpan.ANNOTATION_NEW_OR_CONTINUE_SPAN.wrap(span).event(name);
	}

	String log(final ContinueSpan continueSpan) {
		if (continueSpan != null) {
			return continueSpan.log();
		}
		return "";
	}

	Tracer tracer() {
		if (this.tracer == null) {
			this.tracer = this.beanFactory.getBean(Tracer.class);
		}
		return this.tracer;
	}

	CurrentTraceContext currentTraceContext() {
		if (this.currentTraceContext == null) {
			this.currentTraceContext = this.beanFactory.getBean(CurrentTraceContext.class);
		}
		return this.currentTraceContext;
	}

	NewSpanParser newSpanParser() {
		if (this.newSpanParser == null) {
			this.newSpanParser = this.beanFactory.getBean(NewSpanParser.class);
		}
		return this.newSpanParser;
	}

	SpanTagAnnotationHandler spanTagAnnotationHandler() {
		if (this.spanTagAnnotationHandler == null) {
			this.spanTagAnnotationHandler = new SpanTagAnnotationHandler(this.beanFactory);
		}
		return this.spanTagAnnotationHandler;
	}

	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
