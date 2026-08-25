package com.example.serviceb.dto;

public record DataItemDto(
        String id,
        String name,
        double price,
        int quantity,
        long timestamp
) {}
