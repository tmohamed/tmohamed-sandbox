package com.example.benchmark;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GrpcSimulation extends Simulation {

    // Base HTTP Protocol configuration targeting Aggregator Service
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json");

    // Scenario 2: gRPC Fan-Out
    ScenarioBuilder grpcScenario = scenario("gRPC Fan-Out Benchmark")
            .exec(
                    http("gRPC Call")
                            .get("/benchmark/grpc/req-123")
                            .check(status().is(200))
            );

    {
        setUp(
                // Phase 2: Test gRPC endpoint sequentially after REST completes
                grpcScenario.injectOpen(
                        constantUsersPerSec(100).during(30),     // Warm-up: 10 RPS for 15s to trigger JIT compilation
                        nothingFor(Duration.of(10, ChronoUnit.MILLIS)), // Pause for 5s to allow system stabilization
                        rampUsersPerSec(100).to(150).during(30), // Ramp-up: 10 to 200 RPS over 30s
                        nothingFor(Duration.of(10, ChronoUnit.MILLIS)), // Pause for 5s to allow system stabilization
                        constantUsersPerSec(200).during(60),     // Sustained load: Hold 200 RPS for 60s
                        nothingFor(Duration.of(10, ChronoUnit.MILLIS)), // Pause for 5s to allow system stabilization
                        constantUsersPerSec(250).during(60)     // Sustained load: Hold 200 RPS for 60s
                ).protocols(httpProtocol)
        );
    }
}
