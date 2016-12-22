package io.pivotal.microservices.accounts.trace.jpa;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

/**
 * Trace interceptor for all methods of {@link org.springframework.data.repository.Repository}
 * Can be taken as an example of a custom interceptor which creates its own span to be reported.
 *
 * @author vlotar
 */
@Aspect
public class RepositoryTraceInterceptor extends CustomizableTraceInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTraceInterceptor.class);

	private Tracer tracer;

	@Around("execution(public * org.springframework.data.repository.Repository+.*(..))")
	public Object advice(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Span jpaSpan = getSpan(proceedingJoinPoint);
		Object value = null;
		try {
			value = proceedingJoinPoint.proceed();
			this.tracer.close(jpaSpan);
		} catch (Throwable e) {
			LOGGER.error("Unexpected error occurred", e);
		}
		LOGGER.debug("After invoking getName() method. Return value=" + value);
		return value;
	}

	private Span getSpan(final ProceedingJoinPoint proceedingJoinPoint) {
		if (this.tracer.getCurrentSpan() != null) {
			Span span = this.tracer.createSpan("Repository Method(Query) executed", this.tracer.getCurrentSpan());
			span.tag("Repository Method", proceedingJoinPoint.getSignature().toShortString());
			return span;
		}
		return null;
	}

	@Autowired
	public void setTracer(final Tracer tracer) {
		this.tracer = tracer;
	}
}
