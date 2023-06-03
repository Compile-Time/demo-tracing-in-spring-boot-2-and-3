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

import io.micrometer.tracing.Span;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.NewSpanParser;
import io.micrometer.tracing.internal.SpanNameUtil;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

/**
 * Default implementation of the {@link NewSpanParser} that parses only the span name.
 *
 * @author Christian Schwerdtfeger
 * @since 1.2.0
 */
public class DefaultSpanCreator implements NewSpanParser {

    private static final Log log = LogFactory.getLog(DefaultSpanCreator.class);

    @Override
    public void parse(final MethodInvocation pjp, final NewSpan newSpan, final Span span) {
        final String name = newSpan == null || StringUtils.isEmpty(newSpan.name()) ? pjp.getMethod().getName()
                : newSpan.name();
        final String changedName = SpanNameUtil.toLowerHyphen(name);
        if (log.isDebugEnabled()) {
            log.debug("For the class [" + pjp.getThis().getClass() + "] method " + "[" + pjp.getMethod().getName()
                    + "] will name the span [" + changedName + "]");
        }
        span.name(changedName);
    }

}
