# Microservice Contract Evolution Benchmark: gRPC Protobuf vs. REST/JSON Compatibility

## 1. Purpose & Objectives

In distributed microservice architectures, services deploy independently on separate release cycles. During rolling upgrades, old and new service versions simultaneously communicate across the network.

Traditional REST/JSON APIs require strict versioning (`/v1/`, `/v2/`) or explicit Jackson configuration to prevent deserialization crashes when schemas change. Protobuf handles schema evolution natively at the binary transport layer.

The objectives of this benchmark are:

1. **Validate Backward Compatibility:** Verify that older microservice clients gracefully consume payloads from newer downstream servers without code updates or runtime exceptions.


2. **Validate Forward Compatibility:** Verify that newer microservice clients handle missing fields from older downstream servers without `NullPointerExceptions`.


3. **Contrast with REST/JSON Pitfalls:** Demonstrate how Protobuf binary field tagging decouples code refactoring from wire-level stability.



---

## 2. Schema Evolution Scenario & Rules

The test evaluates a `UserProfile` contract evolving from **V1 (Baseline)** to **V2 (Evolved)**.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                            SCHEMA EVOLUTION MATRIX                          │
├─────────────────────────────────────────────────────────────────────────────┤
│ V1 Contract : user_id (tag 1), username (tag 2), email (tag 3)              │
│ V2 Contract : user_id (tag 1), username (tag 2), tier (tag 4), phone (tag 5) │
│              (Tag 3 reserved to prevent tag collision)                      │
└─────────────────────────────────────────────────────────────────────────────┘

```

### Essential Protobuf Schema Rules Applied

1. **Immutable Tag Numbers:** Numerical tags assigned to fields are never changed or reassigned.


2. **Tag Reservation:** Deprecated fields (`email` / tag 3) are explicitly marked `reserved` to block reuse.


3. **Default Value Guarantees:** Unset fields evaluate to language-level default values (`""` for strings, `0` for numbers).



---

## 3. Empirical Compatibility Test Results

### Test 1: Backward Compatibility (V2 Server → V1 Client)

* **Setup:** Downstream `service-a` emits a **V2 payload** containing new tags `4` (`tier`) and `5` (`phone_number`). `aggregator-service` executes a compiled **V1 stub**.


* **REST/JSON Result:** Fails by default. Jackson throws `com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException` when encountering unknown JSON keys unless `DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES` is explicitly disabled.


* **gRPC/Protobuf Result:** **Passes natively.** V1 stub parses tags `1` and `2`, automatically skipping unknown tags `4` and `5` without errors.



### Test 2: Forward Compatibility (V1 Server → V2 Client)

* **Setup:** Downstream `service-a` emits a **V1 payload** containing tags `1`, `2`, and `3`. `aggregator-service` executes a **V2 stub** expecting tags `1`, `2`, `4`, and `5`.


* **REST/JSON Result:** High defect risk. Missing JSON properties deserialize as Java `null` references, causing downstream `NullPointerExceptions` during business logic execution.


* **gRPC/Protobuf Result:** **Passes natively.** Missing tags `4` and `5` evaluate safely to empty strings `""` rather than `null`, maintaining type safety.



---

## 4. Architectural Comparison Matrix

| Compatibility Scenario | REST / JSON Strategy | gRPC / Protobuf Strategy | Architectural Advantage |
| --- | --- | --- | --- |
| **Unknown Fields** | Throws exception unless explicitly ignored in Jackson.| Ignored at binary frame parser level.| **gRPC** (Zero-config rolling upgrades)
| **Missing Fields** | Maps to `null` references (NPE risk). | Maps to typed default values (`""`, `0`). | **gRPC** (Eliminates `NullPointerException`)
| **Field Renaming** | Breaks JSON key matching (`"username"`).| Preserved via tag number (`tag 2`).| **gRPC** (Safe code refactoring)
| **Polyglot Stubs** | Manual DTO creation across Go/Java/Node repos. | Auto-generated from single `.proto` spec.| **gRPC** (Cross-language consistency)

---

## 5. Conclusion

Protobuf's tag-based binary encoding decouples API evolution from service deployment ordering. By replacing string-based JSON key matching with immutable tag fields, **gRPC enables safe, zero-downtime rolling deployments** across polyglot microservice architectures without requiring brittle REST versioning schemes or explicit deserialization flags.