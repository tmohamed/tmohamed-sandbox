# Microservice Resiliency Benchmark: gRPC Deadline Propagation vs. REST Timeouts

## 1. Purpose & Objectives

In microservice call chains, downstream dependencies can experience latent execution due to slow database queries, resource contention, or locks. When an entry-point service times out, the system must decide how downstream nodes respond.

The objectives of this benchmark are:

1. **Verify Deadline Propagation:** Validate that gRPC automatically propagates remaining client deadlines down the call graph via the `grpc-timeout` HTTP/2 header.


2. **Observe Downstream Stream Cancellation:** Measure how gRPC issues HTTP/2 `RST_STREAM` frames to abort processing immediately when deadlines expire.


3. **Contrast with REST (HTTP/1.1):** Demonstrate how REST client timeouts sever local socket connections while leaving downstream server threads running orphaned execution to completion, wasting CPU and memory resources.

---

## 2. Test Topology & Configuration

The test evaluates an **Aggregator Service** calling a **Downstream Target (`service-a`)** under a forced execution latency condition.

```
┌──────────────────┐               2.0s Deadline Target
│  Client / Curl   │ ─────────────────────────────────────────┐
└──────────────────┘                                          │
                                                              ▼
                                                ┌──────────────────────────┐
                                                │    Aggregator Service    │
                                                └─────────────┬────────────┘
                                                              │
                                       5.0s Slow Task         │ (2.0s Deadline / Timeout)
                                                              ▼
                                                ┌──────────────────────────┐
                                                │   Downstream Service A   │
                                                └──────────────────────────┘

```

* **Aggregator Enforced Limit:** 2,000 ms (2.0 seconds) timeout/deadline.
* **Downstream Task Duration:** 5,000 ms (5.0 seconds) simulated operation executed in 100 ms intervals.

---

## 3. Protocol Execution Mechanics

### gRPC Cancellation Signal Handling

* The aggregator creates a stub configured with a 2-second deadline.


* When 2 seconds elapse, the gRPC framework cancels the call and transmits an HTTP/2 `RST_STREAM` frame to `service-a`.


* Inside `service-a`, the execution loop periodically checks the gRPC context status (`Context.current().isCancelled()`). Upon detecting cancellation, it exits the loop immediately to release thread and CPU resources.



### REST Timeout Handling

* The aggregator configures a WebClient HTTP call with a 2-second timeout duration.
* When 2 seconds elapse, WebClient cancels the client-side mono and closes the TCP socket locally.
* Inside `service-a`, the endpoint thread has no protocol-level mechanism to inspect socket drop events mid-execution. It continues executing its 5-second sleep loop to completion regardless of client presence.

---

## 4. Benchmark Verification Results

### Test 1: gRPC Deadline Propagation & Cancellation

* **Action:** Client triggers gRPC endpoint with a **2.0-second stub deadline** calling a **5.0-second downstream task**.
* **Client Response:** Returns `HTTP/1.1 544 Gateway Timeout` at **2,000 ms**.
* **`service-a` Container Log Output:**

```text
[gRPC Server] Started processing slow query for ID: 1
>>> [gRPC Server] CANCELLED! Upstream deadline exceeded at 2000ms. Aborting work.

```

**Result:** At the exact 2.0-second mark, the aggregator's gRPC client issued an HTTP/2 `RST_STREAM` frame. `service-a` detected context cancellation instantly, exited its processing loop, and **aborted the remaining 3,000 ms of work**, releasing container worker threads and CPU cycles.

---

### Test 2: REST Timeout (HTTP/1.1)

* **Action:** Client triggers REST endpoint with a **2.0-second WebClient timeout** calling a **5.0-second downstream task**.
* **Client Response:** Returns `HTTP/1.1 544 Gateway Timeout` at **2,000 ms**.
* **`service-a` Container Log Output:**

```text
[REST Server] Started processing slow query for ID: 1
>>> [REST Server] FINISHED 5-second task completely (CPU cycles wasted even if client left).

```

**Result:** Although the aggregator abandoned the call at 2.0 seconds and dropped the local socket, `service-a` received no cancellation signal. The downstream thread **executed for the full 5.0 seconds**, wasting 3.0 seconds of CPU and memory processing an orphaned request.

---

## 5. Resiliency Comparison Matrix

| Resiliency Feature | REST (HTTP/1.1) | gRPC (HTTP/2) | Architectural Advantage |
| --- | --- | --- | --- |
| **Timeout Signal Mechanism** | Socket drop on client side | HTTP/2 `RST_STREAM` frame & `grpc-timeout` header

| **gRPC** |
| **Downstream Awareness** | Blind (Thread unaware of client exit) | Explicit (`Context.current().isCancelled()`)

| **gRPC** |
| **Downstream Execution Post-Timeout** | Runs to completion (Wastes 3,000 ms) | Aborts immediately at 2,000 ms mark | **gRPC** |
| **Wasted Resource Overhead** | High (CPU/RAM bound to orphaned tasks) | Near Zero (Instant thread unmounting) | **gRPC** |
| **Cascading Failure Protection** | Low (Slow downstreams trigger thread starvation across tiers) | High (Resource starvation prevented at source) | **gRPC** |

---

## 6. Conclusion

In high-concurrency microservice architectures, client timeouts without downstream cancellation lead to **resource starvation cascades**.

By leveraging **gRPC Deadline Propagation and HTTP/2 `RST_STREAM` frame cancellations**, downstream nodes terminate orphaned transactions the moment an upstream caller abandons the request. This eliminates wasted CPU cycles and protects deep call graphs from thread starvation under partial degradation.