package com.example.aggregator.controller;

import com.example.aggregator.dto.AggregatedResponseDto;
import com.example.aggregator.dto.DataItemDto;
import com.example.aggregator.dto.DataResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class AggregatorController {
    private final WebClient webClient;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    private com.example.servicea.grpc.generated.DataServiceGrpc.DataServiceBlockingStub serviceAStub;

    @Value("${serviceA.uri}")
    private String serviceAUri;

    private static final Logger log = LoggerFactory.getLogger(AggregatorController.class);

    public AggregatorController(WebClient webClient) {
        this.webClient = webClient;
    }

    // --- REST FAN-OUT BENCHMARK ENDPOINT ---
    @GetMapping("/benchmark/rest/{id}")
    public Mono<AggregatedResponseDto> benchmarkRest(@PathVariable String id) {
        log.info("Processing REST observability request for ID: {}", id); // Log inside active request span

        long startTime = System.currentTimeMillis();

        Mono<DataResponseDto> callA = webClient.get()
                .uri(serviceAUri + id)
                .retrieve()
                .bodyToMono(DataResponseDto.class);

        return callA.map(responseA -> {
            long elapsed = System.currentTimeMillis() - startTime;
            return new AggregatedResponseDto(id, responseA, elapsed);
        });
    }

    // --- gRPC FAN-OUT BENCHMARK ENDPOINT ---
    @GetMapping("/benchmark/grpc/{id}")
    public CompletableFuture<AggregatedResponseDto> benchmarkGrpc(@PathVariable String id) {
        log.info("Processing gRPC observability request for ID: {}", id); // Log inside active request span

        long startTime = System.currentTimeMillis();
        com.example.servicea.grpc.generated.DataRequest requestA = com.example.servicea.grpc.generated.DataRequest.newBuilder().setRequestId(id).build();

        CompletableFuture<com.example.servicea.grpc.generated.DataResponse> futureA = CompletableFuture.supplyAsync(() -> serviceAStub.getData(requestA), executor);

        return CompletableFuture.allOf(futureA).thenApply(v -> {
            DataResponseDto resA = mapToDto(futureA.join());
            long elapsed = System.currentTimeMillis() - startTime;
            return new AggregatedResponseDto(id, resA, elapsed);
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
}
