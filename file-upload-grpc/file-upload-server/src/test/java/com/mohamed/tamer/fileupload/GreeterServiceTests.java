//package com.mohamed.tamer.fileupload;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.grpc.test.autoconfigure.AutoConfigureTestGrpcTransport;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.grpc.client.ImportGrpcClients;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@SpringBootTest
//@AutoConfigureTestGrpcTransport
//@ImportGrpcClients(types = GreeterGrpc.GreeterBlockingStub.class)
//class GreeterServiceTests {
//
//    @Autowired
//    private GreeterGrpc.GreeterBlockingStub greeterStub;
//
//    @Test
//    void sayHello() {
//        HelloRequest request = HelloRequest.newBuilder()
//                .setName("Spring")
//                .build();
//        HelloReply reply = this.greeterStub.sayHello(request);
//        assertThat(reply.getMessage()).isEqualTo("Hello 'Spring'");
//    }
//}
