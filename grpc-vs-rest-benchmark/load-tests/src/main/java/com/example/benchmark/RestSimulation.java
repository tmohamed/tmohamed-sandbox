package com.example.benchmark;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class RestSimulation extends Simulation {

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

    {
        setUp(
                // Phase 1: Test REST endpoint
                restScenario.injectOpen(
                        constantUsersPerSec(10).during(15),     // Warm-up: 10 RPS for 15s to trigger JIT compilation
                        rampUsersPerSec(10).to(100).during(30), // Ramp-up: 10 to 200 RPS over 30s
                        constantUsersPerSec(100).during(60)     // Sustained load: Hold 200 RPS for 60s
                ).protocols(httpProtocol)
        );
    }
}
