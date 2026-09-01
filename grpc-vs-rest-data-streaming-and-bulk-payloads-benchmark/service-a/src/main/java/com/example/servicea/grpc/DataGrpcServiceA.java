package com.example.servicea.grpc;

import com.example.servicea.grpc.generated.*;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class DataGrpcServiceA extends DataServiceGrpc.DataServiceImplBase{

    @Override
    public void exportData(ExportRequest request, StreamObserver<DataRecord> responseObserver) {
        int totalRecords = request.getRecordCount();
        long now = System.currentTimeMillis();

        for (int i = 1; i <= totalRecords; i++) {
            DataRecord record = DataRecord.newBuilder()
                    .setId(i)
                    .setTitle("Bulk Export Data Record #" + i)
                    .setAmount(10.50 + i)
                    .setTimestamp(now)
                    .build();

            // Stream each record over the HTTP/2 frame instantly
            responseObserver.onNext(record);
        }

        responseObserver.onCompleted();
    }
}
