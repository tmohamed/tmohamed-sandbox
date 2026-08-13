package com.mohamed.tamer.fileuploadclient.config;

import com.mohamed.tamer.fileupload.grpc.FileUploadServiceGrpc;
import io.grpc.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    @Bean
    public FileUploadServiceGrpc.FileUploadServiceStub fileUploadServiceStub(GrpcChannelFactory channelFactory){
        Channel channel = channelFactory.createChannel("file_upload");
        return FileUploadServiceGrpc.newStub(channel);
    }
}
