package com.mohamed.tamer.fileuploadclient;

import com.google.protobuf.ByteString;
import com.mohamed.tamer.file_upload_with_timeout.FileUploadWithTimeoutRequest;
import com.mohamed.tamer.file_upload_with_timeout.FileUploadWithTimeoutResponse;
import com.mohamed.tamer.file_upload_with_timeout.FileUploadWithTimeoutServiceGrpc;
import com.mohamed.tamer.file_upload_with_timeout.Metadata2;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class FileUploadWithTimeoutClientService {
    @Autowired
    private FileUploadWithTimeoutServiceGrpc.FileUploadWithTimeoutServiceStub fileUploadServiceStub;


    public CompletableFuture<FileUploadWithTimeoutResponse> uploadFileWithTimeout(File file) {

        var timedStub = fileUploadServiceStub.withDeadlineAfter(3, TimeUnit.SECONDS);

        CompletableFuture<FileUploadWithTimeoutResponse> futureResponse = new CompletableFuture<>();

        // 1. Define response handling
        StreamObserver<FileUploadWithTimeoutResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(FileUploadWithTimeoutResponse response) {
                // Complete the Future when server responds
                futureResponse.complete(response);
            }

            @Override
            public void onError(Throwable t) {
                // 3. HANDLE THE TIMEOUT EXCEPTION
                Status status = Status.fromThrowable(t);

                if (status.getCode() == Status.Code.DEADLINE_EXCEEDED) {
                    System.err.println("TRANSACTION ABORTED: The server took too long to respond.");
                } else {
                    System.err.println("Other gRPC Error: " + status.getDescription());
                }
            }

            @Override
            public void onCompleted() {
                System.out.println("Stream completed normally.");
            }
        };

        // 2. Open client streaming pipeline
        StreamObserver<FileUploadWithTimeoutRequest> requestObserver = timedStub.uploadFileWithTimeout(responseObserver);

        try (InputStream in = new FileInputStream(file)) {
            // Send Metadata Header first
            Metadata2 metadata = Metadata2.newBuilder()
                    .setFileName(file.getName())
                    .setContentType("application/octet-stream")
                    .build();

            requestObserver.onNext(FileUploadWithTimeoutRequest.newBuilder().setMetadata(metadata).build());

            // Stream file content in 64 KB binary chunks
            byte[] buffer = new byte[64 * 1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                FileUploadWithTimeoutRequest chunkRequest = FileUploadWithTimeoutRequest.newBuilder()
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
