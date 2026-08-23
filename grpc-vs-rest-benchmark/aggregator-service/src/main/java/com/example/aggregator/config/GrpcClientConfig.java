package com.example.aggregator.config;

import com.example.servicea.grpc.generated.DataServiceGrpc;
import io.grpc.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    @Bean
    public DataServiceGrpc.DataServiceBlockingStub serviceABlockingStub(GrpcChannelFactory channelFactory){
        Channel channel = channelFactory.createChannel("service-a");
        return  DataServiceGrpc.newBlockingStub(channel);
    }

    @Bean
    public DataServiceGrpc.DataServiceBlockingStub serviceBBlockingStub(GrpcChannelFactory channelFactory){
        Channel channel = channelFactory.createChannel("service-b");
        return  DataServiceGrpc.newBlockingStub(channel);
    }


}
