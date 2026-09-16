package com.example.servicea;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceAApplication {
    public static void main(String[] args) {
        // Force global W3C traceparent propagation before any gRPC channels or servers bind
        if (GlobalOpenTelemetry.get().getClass().getSimpleName().contains("Noop")) {
            OpenTelemetrySdk sdk = OpenTelemetrySdk.builder()
                    .setTracerProvider(SdkTracerProvider.builder().build())
                    .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                    .buildAndRegisterGlobal();
        }

        SpringApplication.run(ServiceAApplication.class, args);
    }
}
