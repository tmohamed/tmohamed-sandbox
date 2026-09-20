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
                grpcScenario.injectOpen(
                        // 1. Warm-up phase: 25 RPS for 15s
                        constantUsersPerSec(25).during(Duration.ofSeconds(15)),
                        nothingFor(Duration.ofSeconds(5)),

                        // 2. Linear ramp-up to 250 RPS over 30s
                        rampUsersPerSec(25).to(250).during(Duration.ofSeconds(30)),

                        // 3. Sustained load: Hold 180 RPS (10,800 RPM) for 3 minutes
                        constantUsersPerSec(180).during(Duration.ofMinutes(3))
                ).protocols(httpProtocol)
        );
    }
}
