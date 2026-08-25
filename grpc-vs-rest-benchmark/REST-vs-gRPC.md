# Microservice Fan-Out Benchmark: REST (HTTP/1.1) vs. gRPC (HTTP/2)

## Executive Summary
This report evaluates the performance differences between **REST (HTTP/1.1 WebClient)** and **gRPC (HTTP/2 with Java 21 Virtual Threads)** in a microservice fan-out architecture under heavy load and constrained CPU environments (1.0 CPU Aggregator, 0.5 CPU Downstream Services).

**Core Finding:** gRPC combined with Virtual Threads is the definitive winner. It maintains stable sub-200ms tail latency at loads where traditional REST suffers total connection pool collapse.

---

## Performance Benchmark Matrix

| Load (RPS) | Metric | REST (HTTP/1.1 WebClient) | gRPC (HTTP/2 + Virtual Threads) | Winner & Performance Delta |
| :--- | :--- | :--- | :--- | :--- |
| **100 RPS** | **Success Rate**<br>**p50 Latency**<br>**p95 Latency**<br>**p99 Latency** | 100%<br>5 ms<br>19 ms<br>141 ms | 100%<br>**4 ms**<br>**15 ms**<br>**56 ms** | **gRPC**<br>gRPC slashes p99 tail latency by **2.5x**. |
| **150 RPS** | **Success Rate**<br>**p50 Latency**<br>**p95 Latency**<br>**p99 Latency** | 100%<br>6 ms<br>78 ms<br>**142 ms** | 100%<br>**5 ms**<br>**74 ms**<br>144 ms | **gRPC**<br>Both remain stable; gRPC maintains tighter median/p95 bounds. |
| **200 RPS** *(Inflection)* | **Success Rate**<br>**p50 Latency**<br>**p95 Latency**<br>**p99 Latency** | **28.01% (71.99% KO)**<br>15,001 ms<br>60,000 ms<br>60,001 ms | **100.00% (0% KO)**<br>**27 ms**<br>**103 ms**<br>**171 ms** | **gRPC (Decisive Win)**<br>REST collapses due to connection pool saturation. gRPC holds 100% success. |
| **250 RPS** *(Overload)* | **Success Rate**<br>**OK Throughput**<br>**Failure Mode** | 17.31%<br>27.48 RPS<br>Socket Drops & HTTP 500s | **29.63%**<br>**44.45 RPS**<br>Graceful Memory Queuing | **gRPC**<br>Under total CPU exhaustion, gRPC processes **+61.7% more traffic**. |

---

## Architectural Analysis

### Why gRPC Prevails in Fan-Out Architecture

* **HTTP/2 Streams Multiplexing:** gRPC reuses persistent HTTP/2 TCP connections to downstream services. It handles hundreds of concurrent fan-out calls without connection pool churn or file descriptor (`ulimit`) exhaustion.
* **Virtual Thread I/O Unmounting:** Executing blocking gRPC stubs via `Executors.newVirtualThreadPerTaskExecutor()` allows worker threads to unmount from carrier threads during network I/O, completely avoiding thread pool starvation on a 1.0 CPU limit.
* **Protobuf Binary Efficiency:** Binary payload encoding consumes significantly fewer CPU cycles than Jackson JSON string parsing, reserving vital CPU headroom for request orchestration.

### Why REST Collapses Under Concurrency

* **HTTP/1.1 Connection Pool Saturation:** Spring's `WebClient` connection pool becomes overwhelmed at 200 RPS (400 downstream requests/sec), resulting in **72% of calls dropping** via `PrematureCloseException`.
* **JSON Serialization Overhead:** Reflection and string parsing during JSON serialization cause heavy CPU throttling inside container bounds, pushing unhandled calls into memory queues until they hit 60-second execution timeouts.

---

## Infrastructure & Test Setup

* **Runtime:** Java 21 / Spring Boot 3.x
* **Deployment:** Docker Compose (`aggregator-service`: 1.0 CPU, `service-a`: 0.5 CPU, `service-b`: 0.5 CPU)
* **Load Generator:** Gatling (Open-system injection profile with 15s JIT warm-up)