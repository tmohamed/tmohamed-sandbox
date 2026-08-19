package com.mohamed.tamer.fileuploadclient;

import com.mohamed.tamer.hello.grpc.HelloReply;
import com.mohamed.tamer.hello.grpc.HelloRequest;
import com.mohamed.tamer.hello.grpc.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloClientService {

    @Autowired
    private HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub;

    @Autowired
    private HelloServiceGrpc.HelloServiceStub helloServiceStub;

//    public String sayHello(String name) {
//
//        HelloRequest request = HelloRequest.newBuilder()
//                .setName(name)
//                .build();
//
//        HelloReply reply = helloServiceStub.sayHello(request);
//        return reply.getMessage();
//    }

    public void sayHello(String name) {

        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();

        helloServiceStub.sayHello(request, new StreamObserver<>() {
            @Override
            public void onNext(HelloReply helloReply) {
                System.out.println("Sent request: " + request.getName());
                System.out.println("Received reply: " + helloReply.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error occurred: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Request completed.");
            }
        });
    }


}
