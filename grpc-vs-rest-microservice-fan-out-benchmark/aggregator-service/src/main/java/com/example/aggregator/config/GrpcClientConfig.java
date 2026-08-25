package com.example.aggregator.config;

import io.grpc.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    @Bean
    public com.example.servicea.grpc.generated.DataServiceGrpc.DataServiceBlockingStub serviceABlockingStub(GrpcChannelFactory channelFactory){
        Channel channel = channelFactory.createChannel("servicea");
        return  com.example.servicea.grpc.generated.DataServiceGrpc.newBlockingStub(channel);
    }

    @Bean
    public com.example.serviceb.grpc.generated.DataServiceGrpc.DataServiceBlockingStub serviceBBlockingStub(GrpcChannelFactory channelFactory){
        Channel channel = channelFactory.createChannel("serviceb");
        return  com.example.serviceb.grpc.generated.DataServiceGrpc.newBlockingStub(channel);
    }
}
