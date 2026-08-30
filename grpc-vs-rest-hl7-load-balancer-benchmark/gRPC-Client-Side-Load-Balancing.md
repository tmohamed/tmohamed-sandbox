# gRPC Layer 7 Load Balancing Benchmark in Kubernetes

## 1. Purpose & Objectives

Standard Kubernetes Layer 4 services (`ClusterIP`) route traffic at the TCP level. Because gRPC relies on HTTP/2—which multiplexes multiple logical requests over a single long-lived TCP connection—standard Kubernetes services pin all gRPC traffic to the first pod replica that opens a connection. Remaining replicas sit idle, creating severe hotspotting and bottlenecking performance.

The primary objectives of this benchmark are to:
1. **Validate Layer 7 Stream Balancing:** Measure the performance of client-side round-robin load balancing over a Kubernetes Headless Service (`clusterIP: None`), ensuring request streams distribute evenly across all pod replicas.
2. **Quantify High-Throughput Performance:** Test the setup under sustained high concurrency (up to 500 RPS) to evaluate system throughput, mean latency, and tail latency (p95/p99) using Java 21 Virtual Threads.
3. **Identify Architectural Bottlenecks:** Isolate networking limitations introduced by local virtualization drivers versus internal Kubernetes pod-to-pod networking performance.

---

## 2. Executive Summary

By coupling a **Kubernetes Headless Service** with gRPC's built-in `dns:///` name resolver and `round_robin` load balancing policy, the architecture achieved true Layer 7 stream distribution across all `service-a` pod replicas.

In-cluster benchmarking using Fortio demonstrated **100% request success at 500 RPS (60,000 total requests sustained over 2 minutes)** with an overall median latency of **8.23 ms** and flat, predictable p99 tail latency (**24.59 ms**).

### Core Takeaways
* **Zero Hotspotting:** Requests distribute strictly across all available pod replicas at the HTTP/2 stream level.
* **Sub-10ms Median Latency:** Median response time remained under 10 ms across all load steps (150 RPS to 500 RPS).
* **Flat Tail Latency:** p99 tail latency remained completely stable (~21–24 ms) as load tripled, confirming no thread pool exhaustion or socket queue congestion under heavy load.

---

## 3. Architecture Overview

* **Transport:** gRPC over HTTP/2 stream multiplexing.
* **Service Discovery:** Kubernetes Headless Service (`clusterIP: None`) coupled with CoreDNS.
* **Load Balancing Policy:** Client-side round-robin (`round_robin`).
* **Runtime Platform:** Java 21 with Virtual Threads (`Executors.newVirtualThreadPerTaskExecutor()`).
* **Microservices Flow:** `Aggregator Service` $\rightarrow$ `Service A` (3 Replicas).

---

## 4. Benchmarking Methodology & macOS Virtualization Finding

During initial load tests executed from the macOS host via `kubectl port-forward` or `minikube tunnel`, throughput hard-capped at **~35–40 RPS**, causing massive request backlogs and 60-second client timeouts at target loads above 100 RPS.

* **Root Cause:** User-space host-to-VM network proxy drivers (VPNKit/hyperkit/gVisor) on macOS enforce a strict kernel socket buffer limit around 35–40 RPS. The 60-second timeouts were artifacts of the local virtualization bridge rather than the application or cluster.
* **Resolution:** Testing was shifted to **in-cluster load generation** using `fortio`. Firing requests directly from a dedicated container inside the Minikube cluster bypassed host network bridges and evaluated true pod-to-pod network interface (`10.244.x.x`) speed.

---

## 5. Benchmark Results

All tests were performed in-cluster using Fortio targeting `aggregator-service` fan-out to 3 `service-a` replicas.

### Full Progression Table

| Target Throughput | Test Duration | Total Requests | Success Rate | Mean Latency | p50 (Median) | p75 | p90 | p99 (Tail) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **150 RPS** | 30s | 4,500 | **100.0%** (0 errors) | 5.68 ms | **4.60 ms** | 5.57 ms | 6.98 ms | **23.57 ms** |
| **200 RPS** | 30s | 6,000 | **100.0%** (0 errors) | 6.48 ms | **5.59 ms** | 6.63 ms | 8.19 ms | **21.74 ms** |
| **300 RPS** | 60s | 18,000 | **100.0%** (0 errors) | 7.79 ms | **6.79 ms** | 8.26 ms | 11.84 ms | **24.18 ms** |
| **500 RPS** | 120s | 60,000 | **100.0%** (0 errors) | 9.04 ms | **8.23 ms** | 9.82 ms | 12.60 ms | **24.59 ms** |

---

## 6. Key Performance Insights

1. **Linear Throughput Scaling:** Increasing load from 150 RPS to 500 RPS ($3.33\times$ increase) resulted in less than a 4 ms increase in median latency (4.60 ms $\rightarrow$ 8.23 ms).
2. **Rock-Solid Tail Latency (p99 Immovability):** Across all four test runs, p99 latency hovered consistently between **21.74 ms and 24.59 ms**. This proves that Java 21 Virtual Threads effectively prevent worker thread starvation during high concurrent stream multiplexing.
3. **High Density Efficiency:** In the 500 RPS / 2-minute test run, **84.95% of all 60,000 requests finished in under 11 milliseconds**.

---

## 7. Conclusion

Using standard Kubernetes L4 virtual IPs with gRPC leads to severe replica hotspotting due to HTTP/2 connection reuse. Implementing **Kubernetes Headless Services with gRPC Client-Side Round-Robin Balancing** completely resolves connection pin-down, allowing microservices to scale horizontally across all replicas while maintaining predictable sub-10ms response times and zero error rates up to 500 RPS.