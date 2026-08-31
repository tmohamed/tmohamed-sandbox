package com.example.aggregator.controller;

import com.example.servicea.grpc.generated.DataRequest;
import com.example.servicea.grpc.generated.DataResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.example.servicea.grpc.generated.DataServiceGrpc;

@RestController
public class AggregatorController {
    private final WebClient webClient;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    private DataServiceGrpc.DataServiceBlockingStub serviceAStub;

    @Value("${serviceA.uri}")
    private String serviceAUri;

    public AggregatorController(WebClient webClient) {
        this.webClient = webClient;
    }

    // 1. gRPC Deadline Test Endpoint
    @GetMapping("/benchmark/deadline/grpc/{id}")
    public CompletableFuture<ResponseEntity<String>> testGrpcDeadline(@PathVariable String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Attach a strict 2-second deadline to the gRPC stub call
                DataServiceGrpc.DataServiceBlockingStub timedStub =
                        serviceAStub.withDeadlineAfter(2, TimeUnit.SECONDS);

                DataResponse response = timedStub.getSlowData(DataRequest.newBuilder().setRequestId(id).build());
                return ResponseEntity.ok(response.getPayload());

            } catch (StatusRuntimeException e) {
                if (e.getStatus().getCode() == Status.Code.DEADLINE_EXCEEDED) {
                    return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                            .body("gRPC Deadline Exceeded (2s limit reached)");
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }, executor);
    }


    // 2. REST Timeout Test Endpoint
    @GetMapping("/benchmark/deadline/rest/{id}")
    public Mono<ResponseEntity<String>> testRestTimeout(@PathVariable String id) {
        return webClient.get()
                .uri(serviceAUri + id)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(2)) // 2-second timeout on WebClient
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                        .body("REST Timeout Exceeded (2s limit reached)"));
    }
}
