package com.example.servicea.grpc;

import com.example.servicea.grpc.generated.DataRequest;
import com.example.servicea.grpc.generated.DataResponse;
import com.example.servicea.grpc.generated.DataServiceGrpc;
import com.example.servicea.service.DataServiceA;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class DataGrpcServiceA extends DataServiceGrpc.DataServiceImplBase{
    private final DataServiceA mockService;

    public DataGrpcServiceA(DataServiceA mockService) {
        this.mockService = mockService;
    }

    @Override
    public void getData(DataRequest request, StreamObserver<DataResponse> responseObserver) {
        DataResponse response = mockService.getGrpcData(request.getRequestId());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
