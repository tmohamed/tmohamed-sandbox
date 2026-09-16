package com.example.servicea.config;

import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.otel.bridge.OtelBaggageManager;
import io.micrometer.tracing.otel.bridge.OtelCurrentTraceContext;
import io.micrometer.tracing.otel.bridge.OtelTracer;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class TracingConfig {

    @Bean
    @ConditionalOnMissingBean
    public OpenTelemetry openTelemetry() {
        OpenTelemetrySdk sdk = OpenTelemetrySdk.builder()
                .setTracerProvider(SdkTracerProvider.builder().build())
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .build();

        try {
            GlobalOpenTelemetry.set(sdk);
        } catch (IllegalStateException e) {
            // Handle re-initialization gracefully in test environments
        }
        return sdk;
    }

    @Bean
    @ConditionalOnMissingBean
    public io.opentelemetry.api.trace.Tracer openTelemetryTracer(OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer("grpc-benchmark");
    }

    @Bean
    @ConditionalOnMissingBean
    public Tracer micrometerTracer(io.opentelemetry.api.trace.Tracer otelTracer) {
        OtelCurrentTraceContext currentTraceContext = new OtelCurrentTraceContext();
        OtelBaggageManager baggageManager = new OtelBaggageManager(
                currentTraceContext, Collections.emptyList(), Collections.emptyList());
        return new OtelTracer(otelTracer, currentTraceContext, event -> {}, baggageManager);
    }
}