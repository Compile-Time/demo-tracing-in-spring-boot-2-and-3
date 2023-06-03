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

package com.example.custominstrumentation.annotation;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.ContinueSpan;
import io.micrometer.tracing.annotation.NewSpan;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

/**
 * Method Invocation processor for non reactor apps.
 *
 * @author Marcin Grzejszczak
 * @since 2.1.0
 */
public class NonReactorSleuthMethodInvocationProcessor extends AbstractMicrometerMethodInvocationProcessor {

	@Override
	public Object process(final MethodInvocation invocation, final NewSpan newSpan, final ContinueSpan continueSpan) throws Throwable {
		return proceedUnderSynchronousSpan(invocation, newSpan, continueSpan);
	}

	private Object proceedUnderSynchronousSpan(final MethodInvocation invocation, final NewSpan newSpan, final ContinueSpan continueSpan)
			throws Throwable {
		Span span = tracer().currentSpan();
		// in case of @ContinueSpan and no span in tracer we start new span and should
		// close it on completion
		final boolean startNewSpan = newSpan != null || span == null;
		if (startNewSpan) {
			span = SleuthAnnotationSpan.ANNOTATION_NEW_OR_CONTINUE_SPAN.wrap(tracer().nextSpan());
			newSpanParser().parse(invocation, newSpan, span);
			span.start();
		}
		final String log = log(continueSpan);
		final boolean hasLog = StringUtils.hasText(log);
		try (final Tracer.SpanInScope scope = tracer().withSpan(span)) {
			before(invocation, span, log, hasLog);
			return invocation.proceed();
		} catch (final Exception ex) {
			onFailure(span, log, hasLog, ex);
			throw ex;
		} finally {
			after(span, startNewSpan, log, hasLog);
		}
	}

}
