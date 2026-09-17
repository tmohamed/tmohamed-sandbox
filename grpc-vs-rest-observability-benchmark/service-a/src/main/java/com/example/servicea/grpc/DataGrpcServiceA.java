package com.example.servicea.grpc;

import com.example.servicea.config.GrpcSecurityInterceptor;
import com.example.servicea.grpc.generated.DataRequest;
import com.example.servicea.grpc.generated.DataResponse;
import com.example.servicea.grpc.generated.DataServiceGrpc;
import com.example.servicea.service.DataServiceA;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService(interceptors = {GrpcSecurityInterceptor.class})
public class DataGrpcServiceA extends DataServiceGrpc.DataServiceImplBase{
    private final DataServiceA mockService;

    private static final Logger log = LoggerFactory.getLogger(DataGrpcServiceA.class);

    public DataGrpcServiceA(DataServiceA mockService) {
        this.mockService = mockService;
    }

    @Override
    public void getData(DataRequest request, StreamObserver<DataResponse> responseObserver) {
        log.info("Processing gRPC observability request for ID: {}", request.getRequestId()); // Log inside active request span

        DataResponse response = mockService.getGrpcData(request.getRequestId());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
