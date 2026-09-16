package com.example.servicea.config;

import io.grpc.*;
import org.slf4j.MDC;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.stereotype.Component;

@Component
@GlobalServerInterceptor
public class W3CServerTraceInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> TRACE_PARENT_HEADER =
            Metadata.Key.of("traceparent", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        String traceparent = headers.get(TRACE_PARENT_HEADER);

        if (traceparent != null && traceparent.startsWith("00-")) {
            String[] parts = traceparent.split("-");
            if (parts.length >= 3) {
                MDC.put("traceId", parts[1]); // Extract traceId from W3C header
                MDC.put("spanId", parts[2]);  // Extract spanId from W3C header
            }
        }

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(next.startCall(call, headers)) {
            @Override
            public void onComplete() {
                try {
                    super.onComplete();
                } finally {
                    MDC.remove("traceId");
                    MDC.remove("spanId");
                }
            }
        };
    }
}
