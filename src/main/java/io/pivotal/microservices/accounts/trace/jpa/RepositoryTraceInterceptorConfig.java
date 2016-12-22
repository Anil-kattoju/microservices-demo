package io.pivotal.microservices.accounts.trace.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AOP Config in order to introduce interceptor for {@link org.springframework.data.repository.Repository}
 *
 * @author vlotar
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class RepositoryTraceInterceptorConfig {

	@Bean
	public RepositoryTraceInterceptor getRepositoryTraceInterceptor() {
		return new RepositoryTraceInterceptor();
	}
}
