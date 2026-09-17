# Master Architectural & Protocol Benchmark Report: All 6 Use Cases

This report aggregates empirical performance, resilience, security, and observability benchmarks evaluating **REST (HTTP/1.1 + JSON)** against **gRPC (HTTP/2 + Protobuf)** across all six core microservice operational use cases.

---

## Benchmark Master Summary Matrix

| Use Case # | Architectural Domain | REST Performance (HTTP/1.1) | gRPC Performance (HTTP/2) | Winning Protocol | Core Operational Advantage |
| --- | --- | --- | --- | --- | --- |
| **1**<br> | **High-Concurrency Fan-Out** | Total connection pool collapse at 200 RPS (72% KO) | 100% success at 200 RPS; 27ms p50 / 171ms p99 | **gRPC**<br> | Multiplexed HTTP/2 streams prevent connection pool exhaustion.
| **2**<br> | **Bulk Data Streaming** | 10.27s (NDJSON) / 2.44s (Pagination); 9.67 MB wire size | 2.24s duration; <2.0ms TTFB; ~4.5 MB wire size | **gRPC**<br> | 53% payload reduction & 4.6x faster than NDJSON.
| **3**<br> | **Kubernetes L7 Balancing** | L4 ClusterIP introduces pod replica hotspotting | 100% success at 500 RPS; 8.23ms p50 / 24.59ms p99 | **gRPC**<br> | Headless service + client-side round-robin eliminates hotspots.
| **4**<br> | **Cascading Resiliency** | Client drops socket; downstream runs 5s to completion | Downstream receives `RST_STREAM` & cancels work at 2s | **gRPC**<br> | Prevents resource starvation by aborting orphaned execution.
| **5**<br> | **Contract Evolution** | Throws `UnrecognizedPropertyException` / NPE risk | Ignores unknown tags; maps missing fields to defaults | **gRPC**<br> | Immutable tag fields enable zero-downtime rolling upgrades.
| **6**<br> | **Observability & Security** | Manual tracing configs; HTTP Servlet filter chain | Zero-code OTel agent; gRPC transport interceptor | **gRPC**<br> | Transport-level auth & automated MDC trace correlation.

---

## 1. High-Concurrency Microservice Fan-Out & Throughput

This use case measures the resiliency and latency profile of an edge `aggregator-service` executing non-blocking fan-out calls to downstream services under constrained CPU environments (1.0 CPU Aggregator, 0.5 CPU downstream).

```
┌──────────────────┐               Parallel Downstream Calls               ┌──────────────┐
│  Gatling Load    │ ────────────────────────────────────────────────────> │ Aggregator   │
└──────────────────┘                                                       └──────┬───────┘
                                                                                  │
                                          ┌───────────────────────────────────────┴───────────────────────────────────────┐
                                          │ (gRPC: 1 Multiplexed TCP Connection)   │ (REST: Connection Pool Contention)    │
                                          ▼                                                                               ▼
                               ┌─────────────────────┐                                                         ┌─────────────────────┐
                               │  Service A (0.5 CPU) │                                                         │  Service B (0.5 CPU) │
                               └─────────────────────┘                                                         └─────────────────────┘

```

* **Baseline Load (100–150 RPS):** Both protocols maintained 100% success rates. However, gRPC reduced p99 tail latency from 141 ms (REST) down to 56 ms (gRPC) at 100 RPS due to binary encoding and unmounting Java 21 Virtual Threads during network I/O.


* **System Inflection Point (200 RPS):** Spring's `WebClient` connection pool experienced complete saturation, leading to **71.99% request failure** via `PrematureCloseException` and median response times soaring to 15,001 ms. Conversely, gRPC maintained **100.00% success** with a median latency of 27 ms and a p99 tail latency of 171 ms.


* **System Overload (250 RPS):** Under extreme CPU exhaustion, gRPC processed **61.7% more successful throughput** (44.45 RPS vs 27.48 RPS) through graceful memory queuing rather than abrupt socket drops.


* **Protocol Winner:** **gRPC**. HTTP/2 stream multiplexing eliminates connection pool churn and file descriptor exhaustion.



---

## 2. Bulk Data Streaming & Wire Payload Efficiency

This scenario benchmarks the export of **100,000 structured data records** (~10MB uncompressed JSON) from `service-a` to evaluate JVM memory footprint, time to first byte (TTFB), and total transfer duration under container limits (1.0 CPU, 512MB RAM).

* **gRPC Server Streaming (`returns (stream DataRecord)`):** Completed the full 100,000-record transfer in **2.24 seconds** with a sub-2.0 ms TTFB. Binary Protobuf serialization bypassed object graph buffering, streaming byte buffers directly to network frames.


* **REST NDJSON Streaming:** WebFlux delivered a fast TTFB (3.7 ms) using HTTP `Transfer-Encoding: chunked`, but line-by-line JSON string formatting created severe CPU bottlenecking, extending total duration to **10.27 seconds**.


* **REST Pagination Loops (100 Pages x 1,000 Records):** Achieved a 2.44-second transfer time but required 100 sequential HTTP request/response cycles, introducing significant socket pool churn.


* **Bandwidth Efficiency:** Protobuf field tagging compressed the wire payload to **~4.5 MB**, representing a **53% bandwidth reduction** compared to REST's 9.67 MB JSON payloads.


* **Protocol Winner:** **gRPC Server Streaming**. Combines the fastest total completion time, lowest network overhead, and zero heap array buffering.



---

## 3. Kubernetes Layer 7 Client-Side Load Balancing

Standard Kubernetes `ClusterIP` services route traffic at Layer 4 (TCP). Because gRPC reuses long-lived HTTP/2 TCP connections, standard L4 load balancing pins all traffic to the first pod replica, causing idle downstream instances and severe hotspotting.

```
                                    ┌────────────────────────────────────────────────────────┐
                                    │        GPRC CLIENT-SIDE ROUND-ROBIN BALANCING          │
                                    └───────────────────────────┬────────────────────────────┘
                                                                │
                                                CoreDNS Headless Resolution
                                                ("dns:///service-a:9091")
                                                                │
                                ┌───────────────────────────────┼───────────────────────────────┐
                                │ Stream 1                      │ Stream 2                      │ Stream 3
                                ▼                               ▼                               ▼
                      ┌──────────────────┐            ┌──────────────────┐            ┌──────────────────┐
                      │ Service A (Pod 1)│            │ Service A (Pod 2)│            │ Service A (Pod 3)│
                      └──────────────────┘            └──────────────────┘            └──────────────────┘

```

* **Architecture Implementation:** Coupled a Kubernetes **Headless Service** (`clusterIP: None`) with gRPC's `dns:///` name resolver and `round_robin` load balancing policy.


* **In-Cluster Load Profile (Fortio Benchmark):** Firing requests directly inside the cluster bypassed macOS host virtualization proxy bottlenecks (which cap at ~35–40 RPS due to socket buffer limits).


* **Sustained Scale Results:** Under 500 RPS (60,000 total requests over 2 minutes), the setup achieved **100% request success** with an 8.23 ms median latency and a flat p99 tail latency of **24.59 ms**.


* **Protocol Winner:** **gRPC with Headless Service**. Guarantees uniform L7 stream distribution across all pod replicas without requiring dedicated service mesh sidecars.



---

## 4. Cascading Resiliency & Deadline Propagation

In deep call chains, slow downstream execution can trigger cascading failures. This test evaluates how protocols handle a 2.0-second aggregator timeout calling a 5.0-second downstream task in `service-a`.

```
Client Timeout (2.0s Enforced)
       │
       ▼
┌──────────────┐    1. HTTP/2 RST_STREAM Frame Issued (at 2.0s)    ┌──────────────┐
│ Aggregator   │ ────────────────────────────────────────────────> │  Service A   │
└──────────────┘                                                   └──────┬───────┘
                                                                          │
                                                                          ▼
                                                       2. Context.current().isCancelled()
                                                          Aborts remaining 3,000ms work!

```

* **gRPC Execution Mechanics:** At the 2.0-second mark, gRPC transmits an HTTP/2 `RST_STREAM` frame to `service-a`. The downstream service monitors `Context.current().isCancelled()`, detects cancellation instantly, and **aborts the remaining 3,000 ms of work**.


* **REST Execution Mechanics:** WebClient drops the client-side TCP socket at 2.0 seconds. However, the downstream REST thread has no protocol mechanism to detect socket drops mid-execution and **runs to completion for the full 5.0 seconds**, wasting 3.0 seconds of CPU/RAM on an orphaned request.


* **Protocol Winner:** **gRPC**. Native deadline propagation (`grpc-timeout`) and `RST_STREAM` frame cancellation eliminate thread starvation and protect downstream systems from cascading degradation.



---

## 5. Microservice Contract Evolution & Schema Compatibility

This benchmark evaluates contract refactoring from **V1 (Baseline)** to **V2 (Evolved)** across rolling microservice deployments.

* **Backward Compatibility (V2 Server $\rightarrow$ V1 Client):** `service-a` emits a V2 payload with new field tags `4` (`tier`) and `5` (`phone_number`).


* *REST/JSON:* Fails by default with `UnrecognizedPropertyException` unless Jackson is explicitly reconfigured.


* *gRPC/Protobuf:* Passes natively; the V1 stub parses known tags and skips unknown binary tags without runtime errors.




* **Forward Compatibility (V1 Server $\rightarrow$ V2 Client):** `service-a` emits a V1 payload lacking new tags expected by a V2 client stub.


* *REST/JSON:* Missing fields map to `null`, creating high `NullPointerException` risks in business logic.


* *gRPC/Protobuf:* Missing tags automatically evaluate to typed defaults (`""` for strings, `0` for numbers), maintaining strict type safety.




* **Protocol Winner:** **gRPC**. Tag-based binary encoding decouples deployment ordering and enables zero-downtime rolling upgrades.



---

## 6. Zero-Code Observability & Security Context Preservation

Evaluating distributed tracing, log context correlation, and service-to-service security without polluting application source code.

* **Zero-Code Observability:** Attached the OpenTelemetry Java Agent (`-javaagent`) at JVM boot. The agent auto-instruments gRPC channels and Netty transports at the bytecode level, extracting and injecting W3C `traceparent` headers into gRPC metadata. All manual tracing dependencies (`micrometer-observation`, `opentelemetry-api`) were purged from `pom.xml`. Logback MDC pattern `[%X{trace_id:-}]` outputs identical 32-character trace IDs across cross-service logs.


* **Hardened gRPC Transport Security:** Implemented `GrpcSecurityInterceptor` to extract `Authorization: Bearer <token>` metadata and validate credentials against a Spring Security `AuthenticationManager` (`DaoAuthenticationProvider`).


* **Async Thread Context Safety:** Used a `ForwardingServerCallListener` wrapper to bind `SecurityContextHolder` during listener execution callbacks (`onHalfClose`/`onMessage`) and purge context in a `finally` block, preventing security credentials from leaking across pooled executor threads.


* **Protocol Winner:** **gRPC + OpenTelemetry Java Agent**. Delivers transport-level authentication and thread-safe distributed tracing with zero custom tracing code.



---

## Comprehensive Architectural Decision Matrix

| Operational Dimension | REST (HTTP/1.1 + JSON) | gRPC (HTTP/2 + Protobuf) | Architectural Guidance |
| --- | --- | --- | --- |
| **Edge API Compatibility** | Universal browser & 3rd-party support | Requires generated stubs & Web proxy | **REST** for North-South Edge APIs
| **Internal Transport Efficiency** | High JSON parsing & connection overhead | Compact binary encoding & HTTP/2 streams | **gRPC** for East-West Microservices
| **High Concurrency / Fan-Out** | Vulnerable to connection pool saturation | Multiplexed non-blocking async execution | **gRPC** for high-throughput aggregation
| **Bulk Data Streaming** | High memory allocation & long serialization | Zero-allocation binary frame streaming | **gRPC** for large exports & data streams
| **Kubernetes Load Balancing** | Handled natively by L4 ClusterIP | Requires Headless Service + Client Balancer | **gRPC** + L7 Headless Service
| **Fault Resilience** | Client-side socket drops leave orphaned tasks | Downstream stream cancellation (`RST_STREAM`) | **gRPC** for cascading failure defense
| **Contract Evolution** | Requires manual versioning (`/v1/`) | Immutable tag fields prevent breaking changes | **gRPC** for seamless rolling deployments
| **Observability & Tracing** | Auto-instrumented via HTTP headers | Auto-instrumented via HTTP/2 metadata | **Tie** (Both support OpenTelemetry agent)