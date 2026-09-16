package com.example.aggregator.config;

import io.grpc.*;
import io.micrometer.tracing.Tracer;
import org.springframework.stereotype.Component;

@Component
public class W3CClientTraceInterceptor implements ClientInterceptor {

    private static final Metadata.Key<String> TRACE_PARENT_HEADER =
            Metadata.Key.of("traceparent", Metadata.ASCII_STRING_MARSHALLER);

    private final Tracer tracer;

    public W3CClientTraceInterceptor(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                var currentSpan = tracer.currentSpan();
                if (currentSpan != null) {
                    // W3C traceparent format: 00-{traceId}-{spanId}-01
                    String traceparent = String.format("00-%s-%s-01",
                            currentSpan.context().traceId(),
                            currentSpan.context().spanId());
                    headers.put(TRACE_PARENT_HEADER, traceparent);
                }
                super.start(responseListener, headers);
            }
        };
    }
}
