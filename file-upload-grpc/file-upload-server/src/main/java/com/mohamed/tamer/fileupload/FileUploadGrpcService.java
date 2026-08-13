package com.mohamed.tamer.fileupload;

import com.mohamed.tamer.fileupload.grpc.FileUploadRequest;
import com.mohamed.tamer.fileupload.grpc.FileUploadResponse;
import com.mohamed.tamer.fileupload.grpc.FileUploadServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@GrpcService
public class FileUploadGrpcService extends FileUploadServiceGrpc.FileUploadServiceImplBase {

    private static final Path UPLOAD_DIR = Paths.get("/Users/tamer-abdeltawab/Desktop/TEMP/8-2026/13/grpc-uploads");

    public FileUploadGrpcService() {
        UPLOAD_DIR.toFile().mkdirs();
    }


    @Override
    public StreamObserver<FileUploadRequest> uploadFile(StreamObserver<FileUploadResponse> responseObserver) {
        return new StreamObserver<>() {
            private OutputStream outputStream;
            private String fileName;
            private long totalBytes = 0;

            @Override
            public void onNext(FileUploadRequest request) {
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
                // Send single response once upload completes
                FileUploadResponse response = FileUploadResponse.newBuilder()
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
