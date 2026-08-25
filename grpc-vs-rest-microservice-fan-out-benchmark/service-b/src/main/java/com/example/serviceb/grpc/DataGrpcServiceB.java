package com.example.serviceb.grpc;

import com.example.serviceb.grpc.generated.DataRequest;
import com.example.serviceb.grpc.generated.DataResponse;
import com.example.serviceb.grpc.generated.DataServiceGrpc;
import com.example.serviceb.service.DataServiceB;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class DataGrpcServiceB extends DataServiceGrpc.DataServiceImplBase{
    private final DataServiceB mockService;

    public DataGrpcServiceB(DataServiceB mockService) {
        this.mockService = mockService;
    }

    @Override
    public void getData(DataRequest request, StreamObserver<DataResponse> responseObserver) {
        DataResponse response = mockService.getGrpcData(request.getRequestId());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
