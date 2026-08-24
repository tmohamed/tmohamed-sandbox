package com.example.benchmark;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class BenchmarkSimulation extends Simulation {

    // Base HTTP Protocol configuration targeting Aggregator Service
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json");

    // Scenario 1: REST Fan-Out
    ScenarioBuilder restScenario = scenario("REST Fan-Out Benchmark")
            .exec(
                    http("REST Call")
                            .get("/benchmark/rest/req-123")
                            .check(status().is(200))
            );

    // Scenario 2: gRPC Fan-Out
    ScenarioBuilder grpcScenario = scenario("gRPC Fan-Out Benchmark")
            .exec(
                    http("gRPC Call")
                            .get("/benchmark/grpc/req-123")
                            .check(status().is(200))
            );

    {
        setUp(
                // Phase 1: Test REST endpoint
                restScenario.injectOpen(
                        rampUsersPerSec(10).to(500).during(30), // Ramp up to 500 RPS over 30s
                        constantUsersPerSec(500).during(60)     // Hold 500 RPS for 60s
                ).protocols(httpProtocol),

                // Phase 2: Test gRPC endpoint sequentially after REST completes
                grpcScenario.injectOpen(
                        nothingFor(100),                         // Wait for REST test + cool-down
                        rampUsersPerSec(10).to(500).during(30),  // Ramp up
                        constantUsersPerSec(500).during(60)      // Hold
                ).protocols(httpProtocol)
        );
    }
}
