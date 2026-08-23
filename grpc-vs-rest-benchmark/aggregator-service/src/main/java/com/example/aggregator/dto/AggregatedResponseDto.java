package com.example.aggregator.dto;

public record AggregatedResponseDto(
        String requestId,
        DataResponseDto sourceA,
        DataResponseDto sourceB,
        long totalProcessingTimeMs
) {}
