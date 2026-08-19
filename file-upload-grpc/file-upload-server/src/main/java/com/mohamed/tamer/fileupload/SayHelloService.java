package com.mohamed.tamer.fileupload;

import com.mohamed.tamer.hello.grpc.HelloReply;
import com.mohamed.tamer.hello.grpc.HelloRequest;
import com.mohamed.tamer.hello.grpc.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class SayHelloService extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void sayHello(HelloRequest request,
                         StreamObserver<HelloReply> responseObserver) {
        String greeting = "Hello, " + request.getName() + "!";
        HelloReply response = HelloReply.newBuilder()
                .setMessage(greeting)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
