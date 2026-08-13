package com.mohamed.tamer.fileuploadclient;

import com.google.protobuf.ByteString;
import com.mohamed.tamer.fileupload.grpc.FileUploadRequest;
import com.mohamed.tamer.fileupload.grpc.FileUploadResponse;
import com.mohamed.tamer.fileupload.grpc.Metadata;
import io.grpc.stub.StreamObserver;
import com.mohamed.tamer.fileupload.grpc.FileUploadServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@Service
public class FileUploadClientService {
    @Autowired
    private FileUploadServiceGrpc.FileUploadServiceStub fileUploadServiceStub;


    public CompletableFuture<FileUploadResponse> uploadFile(File file) {
        CompletableFuture<FileUploadResponse> futureResponse = new CompletableFuture<>();

        // 1. Define response handling
        StreamObserver<FileUploadResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(FileUploadResponse response) {
                // Complete the Future when server responds
                futureResponse.complete(response);
            }

            @Override
            public void onError(Throwable t) {
                // Pass gRPC exceptions down to the caller
                futureResponse.completeExceptionally(t);
            }

            @Override
            public void onCompleted() {
                // Stream closed by server
            }
        };

        // 2. Open client streaming pipeline
        StreamObserver<FileUploadRequest> requestObserver = fileUploadServiceStub.uploadFile(responseObserver);

        try (InputStream in = new FileInputStream(file)) {
            // Send Metadata Header first
            Metadata metadata = Metadata.newBuilder()
                    .setFileName(file.getName())
                    .setContentType("application/octet-stream")
                    .build();

            requestObserver.onNext(FileUploadRequest.newBuilder().setMetadata(metadata).build());

            // Stream file content in 64 KB binary chunks
            byte[] buffer = new byte[64 * 1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                FileUploadRequest chunkRequest = FileUploadRequest.newBuilder()
                        .setChunk(ByteString.copyFrom(buffer, 0, bytesRead))
                        .build();
                requestObserver.onNext(chunkRequest);
            }

            // Signal stream completion to server
            requestObserver.onCompleted();

        } catch (Exception e) {
            requestObserver.onError(e);
            futureResponse.completeExceptionally(e);
        }

        return futureResponse;
    }

}
