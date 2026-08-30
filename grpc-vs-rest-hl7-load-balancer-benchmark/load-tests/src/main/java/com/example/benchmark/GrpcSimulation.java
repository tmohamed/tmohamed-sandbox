package com.example.benchmark;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

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

    // Scenario 2: gRPC Client-Side Load balancing
    ScenarioBuilder grpcScenario = scenario("gRPC Client-Side Load balancing Benchmark")
            .exec(
                    http("gRPC Call")
                            .get("/benchmark/grpc/req-123")
                            .check(status().is(200))
            );

    {
        setUp(
                // Phase 2: Test gRPC endpoint sequentially after REST completes
                grpcScenario.injectOpen(
                        constantUsersPerSec(10).during(15),     // Warm-up: 10 RPS for 15s to trigger JIT compilation
                        rampUsersPerSec(10).to(150).during(30), // Ramp-up: 10 to 200 RPS over 30s
                        constantUsersPerSec(150).during(60)     // Sustained load: Hold 200 RPS for 60s
                ).protocols(httpProtocol)
        );
    }
}
