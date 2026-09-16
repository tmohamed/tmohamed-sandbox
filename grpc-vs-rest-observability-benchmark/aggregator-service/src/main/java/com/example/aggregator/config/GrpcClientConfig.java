package com.example.aggregator.config;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {
    @Autowired private W3CClientTraceInterceptor w3CClientTraceInterceptor;

    @Bean
    public com.example.servicea.grpc.generated.DataServiceGrpc.DataServiceBlockingStub serviceABlockingStub(GrpcChannelFactory channelFactory){
        Channel channel = channelFactory.createChannel("servicea");
        return  com.example.servicea.grpc.generated.DataServiceGrpc.newBlockingStub(channel)
                .withInterceptors(w3CClientTraceInterceptor);
    }
}
