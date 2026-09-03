package com.example.servicea.config;

import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.BindableService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcReflectionConfig {
    @Bean
    public BindableService reflectionService() {
        return ProtoReflectionService.newInstance();
    }
}