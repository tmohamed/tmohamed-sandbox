**Architecture & Implementation Report: OpenTelemetry Auto-Instrumentation & gRPC Security**

**System Architecture Overview**

The microservice system (`aggregator-service` and `service-a`) achieves zero-code observability and hardened service-to-service authentication using Spring Boot 3.x, gRPC, and the OpenTelemetry Java Agent.

| Architectural Domain | Strategy & Implementation | Key Operational Benefit |
| --- | --- | --- |
| **Observability** | OpenTelemetry Java Agent (`-javaagent`) attached at JVM boot | Automatic trace generation and W3C `traceparent` context propagation across HTTP and gRPC boundaries with zero application code. |
| **Log Correlation** | Logback MDC pattern `[%X{trace_id:-}]` | Uniform 32-character trace ID injection across all service threads. |
| **Service Security** | Custom `GrpcSecurityInterceptor` tied to Spring Security `AuthenticationManager` | Rejects unauthorized RPC calls with gRPC `UNAUTHENTICATED` status while validating `Bearer` tokens. |
| **Thread Context Security** | `ForwardingServerCallListener` wrapper in gRPC server interceptor | Solves `ThreadLocal` wiping across gRPC executor worker threads and prevents security context bleeding. |

---

**Zero-Code Observability (OpenTelemetry)**

Distributed tracing is offloaded entirely to the OpenTelemetry Java Agent at runtime, eliminating custom tracing code from the Java application codebase.

* **Agent Deployment:** Attached via container environment variable `JAVA_TOOL_OPTIONS=-javaagent:/app/opentelemetry-javaagent.jar`.
* **Agent Configuration:** Network exporters disabled (`OTEL_TRACES_EXPORTER=none`, `OTEL_METRICS_EXPORTER=none`, `OTEL_LOGS_EXPORTER=none`) to isolate local context propagation and log correlation.

---

**Hardened gRPC Security Model**

Authentication is enforced on internal gRPC endpoints using Spring Security integrated directly into the gRPC transport pipeline.

* **Authentication Pipeline:** The `GrpcSecurityInterceptor` extracts the `Authorization: Bearer <token>` header from gRPC metadata and validates credentials via a `ProviderManager` backed by `DaoAuthenticationProvider`.
* **Thread Boundary Management:** Overridden `onHalfClose()` and `onMessage()` listener callbacks bind the `SecurityContext` to the worker thread during RPC execution and clear it in a `finally` block.
* **Client Header Injection:** `aggregator-service` attaches credentials dynamically to outgoing `serviceAStub` calls using `MetadataUtils.newAttachHeadersInterceptor(headers)`.

---

**Security & Trace Verification Matrix**

| Test Scenario | Request Details | Expected Result | Status |
| --- | --- | --- | --- |
| **Unauthenticated gRPC** | Direct `grpcurl` call with missing/invalid `Bearer` token | Status `UNAUTHENTICATED` ("Missing or invalid Authorization header format") | **Passed** |
| **Authenticated gRPC** | `grpcurl` call with valid `Bearer test-secret-password` | Status `OK` with valid JSON response payload | **Passed** |
| **End-to-End Fan-Out** | REST `curl` call to `aggregator-service` (`/benchmark/grpc/123`) | HTTP 200 response with aggregated payload from `service-a` | **Passed** |
| **Trace ID Parity** | Cross-service log verification | Identical 32-character `trace_id` printed across both container console outputs | **Passed** |