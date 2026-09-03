package com.example.aggregator.config;

import io.grpc.ClientInterceptor;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.core.instrument.binder.grpc.ObservationGrpcClientInterceptor;
import org.springframework.grpc.server.GlobalServerInterceptor;

@Configuration
public class GrpcClientTracingConfig {

    @Bean
    @GlobalServerInterceptor
    public ClientInterceptor observationGrpcClientInterceptor(ObservationRegistry observationRegistry) {
        return new ObservationGrpcClientInterceptor(observationRegistry);
    }
}