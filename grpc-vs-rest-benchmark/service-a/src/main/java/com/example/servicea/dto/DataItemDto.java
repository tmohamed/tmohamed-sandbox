package com.example.servicea.dto;

public record DataItemDto(
        String id,
        String name,
        double price,
        int quantity,
        long timestamp
) {}
