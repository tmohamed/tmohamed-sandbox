package com.example.servicea.grpc;

import com.example.servicea.grpc.generated.DataRequest;
import com.example.servicea.grpc.generated.DataResponse;
import com.example.servicea.grpc.generated.DataServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class DataGrpcServiceA extends DataServiceGrpc.DataServiceImplBase{

    @Override
    public void getSlowData(DataRequest request, StreamObserver<DataResponse> responseObserver) {
        System.out.println("[gRPC Server] Started processing slow query for ID: " + request.getRequestId());

        // Simulate a 5-second operation broken into 100ms intervals
        for (int i = 1; i <= 50; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            // CHECK DEADLINE CANCELLATION: Did the upstream client time out?
            if (io.grpc.Context.current().isCancelled()) {
                System.err.println(">>> [gRPC Server] CANCELLED! Upstream deadline exceeded at " + (i * 100) + "ms. Aborting work.");
                return; // Terminate execution immediately to free CPU/RAM
            }
        }

        DataResponse response = DataResponse.newBuilder()
                .setRequestId(request.getRequestId())
                .setPayload("Slow query completed successfully")
                .setTimestamp(System.currentTimeMillis())
                .build();

        System.err.println(">>> [gRPC Server] Slow query completed successfully");

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
