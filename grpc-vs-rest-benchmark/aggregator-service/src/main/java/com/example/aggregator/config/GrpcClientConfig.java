package com.example.aggregator.config;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.client.channels.service-a.address}")
    private String serviceAHost;

    @Value("${grpc.client.channels.service-b.address}")
    private String serviceBHost;

    @Bean
    public com.example.servicea.grpc.generated.DataServiceGrpc.DataServiceBlockingStub serviceABlockingStub(GrpcChannelFactory channelFactory){
        Channel channel = channelFactory.createChannel(serviceAHost);
        return  com.example.servicea.grpc.generated.DataServiceGrpc.newBlockingStub(channel);
    }

    @Bean
    public com.example.serviceb.grpc.generated.DataServiceGrpc.DataServiceBlockingStub serviceBBlockingStub(GrpcChannelFactory channelFactory){
        Channel channel = channelFactory.createChannel(serviceBHost);
        return  com.example.serviceb.grpc.generated.DataServiceGrpc.newBlockingStub(channel);
    }
}
