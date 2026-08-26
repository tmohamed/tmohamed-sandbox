package com.example.aggregator.config;

import io.grpc.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;
import com.example.servicea.grpc.generated.DataServiceGrpc;

@Configuration
public class GrpcClientConfig {

    @Bean
    public DataServiceGrpc.DataServiceBlockingStub serviceABlockingStub(GrpcChannelFactory channelFactory){
        Channel channel = channelFactory.createChannel("servicea");
        return  DataServiceGrpc.newBlockingStub(channel);
    }
}
