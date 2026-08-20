package com.mohamed.tamer.fileupload;

import com.mohamed.tamer.file_upload_with_timeout.FileUploadWithTimeoutRequest;
import com.mohamed.tamer.file_upload_with_timeout.FileUploadWithTimeoutResponse;
import com.mohamed.tamer.file_upload_with_timeout.FileUploadWithTimeoutServiceGrpc;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@GrpcService
public class FileUploadWithTimeoutGrpcService extends FileUploadWithTimeoutServiceGrpc.FileUploadWithTimeoutServiceImplBase {

    private static final Path UPLOAD_DIR = Paths.get("/Users/tamer-abdeltawab/Desktop/TEMP/8-2026/13/grpc-uploads");

    public FileUploadWithTimeoutGrpcService() {
        UPLOAD_DIR.toFile().mkdirs();
    }


    @Override
    public StreamObserver<FileUploadWithTimeoutRequest> uploadFileWithTimeout(StreamObserver<FileUploadWithTimeoutResponse> responseObserver) {
        return new StreamObserver<>() {
            private OutputStream outputStream;
            private String fileName;
            private long totalBytes = 0;

            @Override
            public void onNext(FileUploadWithTimeoutRequest request) {
                try {
                    if (request.hasMetadata()) {
                        // First message: Extract metadata and initialize the stream
                        fileName = request.getMetadata().getFileName();
                        Path targetPath = UPLOAD_DIR.resolve(fileName);
                        outputStream = new FileOutputStream(targetPath.toFile());
                    } else if (request.hasChunk()) {
                        // Subsequent messages: Write raw byte chunks to disk
                        if (outputStream == null) {
                            responseObserver.onError(
                                    Status.INVALID_ARGUMENT
                                            .withDescription("Metadata must be sent first")
                                            .asRuntimeException()
                            );
                            return;
                        }
                        byte[] bytes = request.getChunk().toByteArray();
                        outputStream.write(bytes);
                        totalBytes += bytes.length;
                    }
                } catch (IOException e) {
                    closeStream();
                    responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
                }
            }

            @Override
            public void onError(Throwable t) {
                closeStream();
            }

            @Override
            public void onCompleted() {

                closeStream();

                System.out.println("Server received all data. Simulating a 10-second hang...");

                try {
                    // SIMULATE HANG: Sleep for 10 seconds
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // CHECK CANCELLATION: Did the client give up while we were sleeping?
                if (Context.current().isCancelled()) {
                    System.err.println("Server aborting: Client terminated the transaction.");
                    return; // Stop processing!
                }

                // Send single response once upload completes
                FileUploadWithTimeoutResponse response = FileUploadWithTimeoutResponse.newBuilder()
                        .setFileName(fileName != null ? fileName : "unknown")
                        .setTotalBytes(totalBytes)
                        .setStatus("SUCCESS")
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }

            private void closeStream() {
                if (outputStream != null) {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException ignored) {}
                }
            }
        };
    }

}
