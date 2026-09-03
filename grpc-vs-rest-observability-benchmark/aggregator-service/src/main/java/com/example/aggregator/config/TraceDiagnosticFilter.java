package com.example.aggregator.config;

import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceDiagnosticFilter implements Filter {

    private final Tracer tracer;

    public TraceDiagnosticFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ScopedSpan span = tracer.startScopedSpan("HTTP " + httpRequest.getMethod() + " " + httpRequest.getRequestURI());

        try {
            if (tracer.currentSpan() != null) {
                MDC.put("traceId", tracer.currentSpan().context().traceId());
                MDC.put("spanId", tracer.currentSpan().context().spanId());
            }
            chain.doFilter(request, response);
        } finally {
            span.end();
            MDC.remove("traceId");
            MDC.remove("spanId");
        }
    }
}