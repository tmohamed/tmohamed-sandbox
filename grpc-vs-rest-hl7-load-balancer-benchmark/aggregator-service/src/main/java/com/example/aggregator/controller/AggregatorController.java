package com.example.aggregator.controller;

import com.example.aggregator.dto.AggregatedResponseDto;
import com.example.aggregator.dto.DataItemDto;
import com.example.aggregator.dto.DataResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.servicea.grpc.generated.DataServiceGrpc;
import com.example.servicea.grpc.generated.DataRequest;
import com.example.servicea.grpc.generated.DataResponse;

@RestController
public class AggregatorController {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    private DataServiceGrpc.DataServiceBlockingStub serviceAStub;

    // --- gRPC FAN-OUT BENCHMARK ENDPOINT ---
    @GetMapping("/benchmark/grpc/{id}")
    public CompletableFuture<AggregatedResponseDto> benchmarkGrpc(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        DataRequest requestA = DataRequest.newBuilder().setRequestId(id).build();

        CompletableFuture<DataResponse> futureA = CompletableFuture.supplyAsync(() -> serviceAStub.getData(requestA), executor);

        return CompletableFuture.allOf(futureA).thenApply(v -> {
            DataResponseDto resA = mapToDto(futureA.join());

            long elapsed = System.currentTimeMillis() - startTime;
            return new AggregatedResponseDto(id, resA, elapsed);
        });
    }

    // Helper method to map Protobuf DataResponse into DataResponseDto
    private DataResponseDto mapToDto(DataResponse response) {
        List<DataItemDto> items = response.getItemsList().stream()
                .map(i -> new DataItemDto(
                        i.getId(),
                        i.getName(),
                        i.getPrice(),
                        i.getQuantity(),
                        i.getTimestamp()
                ))
                .toList();

        return new DataResponseDto(response.getRequestId(), items);
    }
}
