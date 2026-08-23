package com.example.aggregator.controller;

import com.example.aggregator.dto.AggregatedResponseDto;
import com.example.aggregator.dto.DataItemDto;
import com.example.aggregator.dto.DataResponseDto;
import com.example.servicea.grpc.generated.DataRequest;
import com.example.servicea.grpc.generated.DataResponse;
import com.example.servicea.grpc.generated.DataServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class AggregatorController {
    private final WebClient webClient;

    @Autowired
    private DataServiceGrpc.DataServiceBlockingStub serviceAStub;

    @Autowired
    private DataServiceGrpc.DataServiceBlockingStub serviceBStub;

    public AggregatorController(WebClient webClient) {
        this.webClient = webClient;
    }

    // --- REST FAN-OUT BENCHMARK ENDPOINT ---
    @GetMapping("/benchmark/rest/{id}")
    public Mono<AggregatedResponseDto> benchmarkRest(@PathVariable String id) {
        long startTime = System.currentTimeMillis();

        Mono<DataResponseDto> callA = webClient.get()
                .uri("http://service-a:8081/api/data/" + id)
                .retrieve()
                .bodyToMono(DataResponseDto.class);

        Mono<DataResponseDto> callB = webClient.get()
                .uri("http://service-b:8082/api/data/" + id)
                .retrieve()
                .bodyToMono(DataResponseDto.class);

        return Mono.zip(callA, callB).map(tuple -> {
            long elapsed = System.currentTimeMillis() - startTime;
            return new AggregatedResponseDto(id, tuple.getT1(), tuple.getT2(), elapsed);
        });
    }

    // --- gRPC FAN-OUT BENCHMARK ENDPOINT ---
    @GetMapping("/benchmark/grpc/{id}")
    public CompletableFuture<AggregatedResponseDto> benchmarkGrpc(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        DataRequest request = DataRequest.newBuilder().setRequestId(id).build();

        CompletableFuture<DataResponse> futureA = CompletableFuture.supplyAsync(() -> serviceAStub.getData(request));
        CompletableFuture<DataResponse> futureB = CompletableFuture.supplyAsync(() -> serviceBStub.getData(request));

        return CompletableFuture.allOf(futureA, futureB).thenApply(v -> {
            DataResponseDto resA = mapToDto(futureA.join());
            DataResponseDto resB = mapToDto(futureB.join());

            long elapsed = System.currentTimeMillis() - startTime;
            return new AggregatedResponseDto(id, resA, resB, elapsed);
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
