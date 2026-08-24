package com.example.aggregator.controller;

import com.example.aggregator.dto.AggregatedResponseDto;
import com.example.aggregator.dto.DataItemDto;
import com.example.aggregator.dto.DataResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private com.example.servicea.grpc.generated.DataServiceGrpc.DataServiceBlockingStub serviceAStub;

    @Autowired
    private com.example.serviceb.grpc.generated.DataServiceGrpc.DataServiceBlockingStub serviceBStub;

    @Value("${serviceA.uri}")
    private String serviceAUri;

    @Value("${serviceB.uri}")
    private String serviceBUri;

    public AggregatorController(WebClient webClient) {
        this.webClient = webClient;
    }

    // --- REST FAN-OUT BENCHMARK ENDPOINT ---
    @GetMapping("/benchmark/rest/{id}")
    public Mono<AggregatedResponseDto> benchmarkRest(@PathVariable String id) {
        long startTime = System.currentTimeMillis();

        Mono<DataResponseDto> callA = webClient.get()
                .uri(serviceAUri + id)
                .retrieve()
                .bodyToMono(DataResponseDto.class);

        Mono<DataResponseDto> callB = webClient.get()
                .uri(serviceBUri + id)
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
        com.example.servicea.grpc.generated.DataRequest requestA = com.example.servicea.grpc.generated.DataRequest.newBuilder().setRequestId(id).build();
        com.example.serviceb.grpc.generated.DataRequest requestB = com.example.serviceb.grpc.generated.DataRequest.newBuilder().setRequestId(id).build();

        CompletableFuture<com.example.servicea.grpc.generated.DataResponse> futureA = CompletableFuture.supplyAsync(() -> serviceAStub.getData(requestA));
        CompletableFuture<com.example.serviceb.grpc.generated.DataResponse> futureB = CompletableFuture.supplyAsync(() -> serviceBStub.getData(requestB));

        return CompletableFuture.allOf(futureA, futureB).thenApply(v -> {
            DataResponseDto resA = mapToDto(futureA.join());
            DataResponseDto resB = mapToDto(futureB.join());

            long elapsed = System.currentTimeMillis() - startTime;
            return new AggregatedResponseDto(id, resA, resB, elapsed);
        });
    }

    // Helper method to map Protobuf DataResponse into DataResponseDto
    private DataResponseDto mapToDto(com.example.servicea.grpc.generated.DataResponse response) {
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

    private DataResponseDto mapToDto(com.example.serviceb.grpc.generated.DataResponse response) {
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
