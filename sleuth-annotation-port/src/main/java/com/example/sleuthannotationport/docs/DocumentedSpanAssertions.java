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
import io.micrometer.tracing.docs.EventValue;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * In order to turn on the assertions you need to either turn on the
 * {@code spring.cloud.sleuth.assertions.enabled} system property or
 * {@code SPRING_CLOUD_SLEUTH_ASSERTIONS_ENABLED} environment variable.
 */
final class DocumentedSpanAssertions {

    static boolean SLEUTH_SPAN_ASSERTIONS_ON = Boolean.parseBoolean(System.getProperty(
            "spring.cloud.sleuth.assertions.enabled", System.getenv("SPRING_CLOUD_SLEUTH_ASSERTIONS_ENABLED") != null
                    ? System.getenv("SPRING_CLOUD_SLEUTH_ASSERTIONS_ENABLED") : "false"));

    private static final Map<String, Pattern> PATTERN_CACHE = new ConcurrentHashMap<>();

    private static final Pattern SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");

    private DocumentedSpanAssertions() {
        throw new IllegalStateException("Can't instantiate utility class");
    }

    static void assertThatKeyIsValid(final String key, final DocumentedSpan documentedSpan) {
        if (SLEUTH_SPAN_ASSERTIONS_ON) {
            final KeyName[] allowedKeys = documentedSpan.getTagKeys();
            if (allowedKeys.length == 0) {
                return;
            }
            final boolean validTagKey = Arrays.stream(allowedKeys)
                    .anyMatch(tagKey -> patternOrValueMatches(key, tagKey.asString())
                            && hasRequiredPrefix(key, documentedSpan.prefix()));
            if (!validTagKey) {
                throw new AssertionError("The key [" + key + "] is invalid. You can use only one matching "
                        + Arrays.stream(allowedKeys).map(KeyName::asString).collect(Collectors.toList())
                        + prefixWarningIfPresent(documentedSpan));
            }
        }
    }

    private static String prefixWarningIfPresent(final DocumentedSpan documentedSpan) {
        return StringUtils.hasText(documentedSpan.prefix())
                ? ". Also it has start with [" + documentedSpan.prefix() + "] prefix" : "";
    }

    static void assertThatKeyIsValid(final KeyName key, final DocumentedSpan documentedSpan) {
        if (SLEUTH_SPAN_ASSERTIONS_ON) {
            final KeyName[] allowedKeys = documentedSpan.getTagKeys();
            if (allowedKeys.length == 0) {
                return;
            }
            if (Arrays.stream(allowedKeys).noneMatch(tagKey -> patternOrValueMatches(key.asString(), tagKey.asString())
                    && hasRequiredPrefix(key.asString(), documentedSpan.prefix()))) {
                throw new AssertionError("The key [" + key.asString() + "] is invalid. You can use only one matching "
                        + Arrays.stream(allowedKeys).map(KeyName::asString).collect(Collectors.toList())
                        + prefixWarningIfPresent(documentedSpan));
            }
        }
    }

    static void assertThatNameIsValid(final String name, final DocumentedSpan documentedSpan) {
        final String allowedName = documentedSpan.getName();
        if (SLEUTH_SPAN_ASSERTIONS_ON && !patternOrValueMatches(name, allowedName)) {
            throw new AssertionError(
                    "The name [" + name + "] is invalid. You can use only one matching [" + allowedName + "]");
        }
    }

    static void assertThatEventIsValid(final String eventValue, final DocumentedSpan documentedSpan) {
        if (SLEUTH_SPAN_ASSERTIONS_ON) {
            final EventValue[] allowed = documentedSpan.getEvents();
            if (allowed.length == 0) {
                return;
            }
            final boolean valid = Arrays.stream(allowed).anyMatch(value -> patternOrValueMatches(eventValue, value.getValue())
                    && hasRequiredPrefix(eventValue, documentedSpan.prefix()));
            if (!valid) {
                throw new AssertionError("The event [" + eventValue + "] is invalid. You can use only one matching "
                        + Arrays.stream(allowed).map(EventValue::getValue).collect(Collectors.toList())
                        + prefixWarningIfPresent(documentedSpan));
            }
        }
    }

    static void assertThatEventIsValid(final EventValue eventValue, final DocumentedSpan documentedSpan) {
        if (SLEUTH_SPAN_ASSERTIONS_ON) {
            final EventValue[] allowed = documentedSpan.getEvents();
            if (allowed.length == 0) {
                return;
            }
            if (Arrays.stream(allowed).noneMatch(value -> patternOrValueMatches(eventValue.getValue(), value.getValue())
                    && hasRequiredPrefix(eventValue.getValue(), documentedSpan.prefix()))) {
                throw new AssertionError(
                        "The event [" + eventValue.getValue() + "] is invalid. You can use only one matching "
                                + Arrays.stream(allowed).map(EventValue::getValue).collect(Collectors.toList())
                                + prefixWarningIfPresent(documentedSpan));
            }
        }
    }

    static void assertThatSpanStartedBeforeEnd(final AssertingSpan span) {
        if (SLEUTH_SPAN_ASSERTIONS_ON && !span.isStarted()) {
            throw new AssertionError("The span was not started, however you're trying to end it");
        }
    }

    private static boolean patternOrValueMatches(final String pickedValue, final String allowedValue) {
        if (allowedValue.contains("%s")) {
            final String stringPattern = escapeSpecialRegexWithSingleEscape(allowedValue).replaceAll("%s", ".*?");
            final Pattern pattern = PATTERN_CACHE.computeIfAbsent(stringPattern, Pattern::compile);
            return pattern.matcher(pickedValue).matches();
        }
        return allowedValue.equals(pickedValue);
    }

    private static boolean hasRequiredPrefix(final String value, final String prefix) {
        if (StringUtils.hasText(prefix)) {
            return value.startsWith(prefix);
        }
        return true;
    }

    private static String escapeSpecialRegexWithSingleEscape(final String str) {
        return SPECIAL_REGEX_CHARS.matcher(str).replaceAll("\\\\$0");
    }

}
