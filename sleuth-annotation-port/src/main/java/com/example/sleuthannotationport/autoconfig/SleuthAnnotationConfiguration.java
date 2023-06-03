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

package com.example.sleuthannotationport.autoconfig;

import com.example.sleuthannotationport.instrumentation.DefaultSpanCreator;
import com.example.sleuthannotationport.instrumentation.NonReactorSleuthMethodInvocationProcessor;
import com.example.sleuthannotationport.instrumentation.SleuthAdvisorConfig;
import com.example.sleuthannotationport.instrumentation.SpelTagValueExpressionResolver;
import io.micrometer.tracing.annotation.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * Auto-configuration} that allows creating spans by means of a {@link NewSpan}
 * annotation. You can annotate classes or just methods. You can also apply this
 * annotation to an interface.
 *
 * @author Christian Schwerdtfeger
 * @author Marcin Grzejszczak
 * @since 1.2.0
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@ConditionalOnProperty(name = {"spring.sleuth.enabled", "spring.sleuth.annotation.enabled"}, matchIfMissing = true)
public class SleuthAnnotationConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public NewSpanParser newSpanParser() {
		return new DefaultSpanCreator();
	}

	@Bean
	@ConditionalOnMissingBean
	public TagValueExpressionResolver spelTagValueExpressionResolver() {
		return new SpelTagValueExpressionResolver();
	}

	@Bean
	@ConditionalOnMissingBean
	public TagValueResolver noOpTagValueResolver() {
		return new NoOpTagValueResolver();
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public SleuthAdvisorConfig sleuthAdvisorConfig() {
		return new SleuthAdvisorConfig();
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@ConditionalOnMissingClass("reactor.core.publisher.Flux")
	public MethodInvocationProcessor nonReactorSleuthMethodInvocationProcessor() {
		return new NonReactorSleuthMethodInvocationProcessor();
	}

}
