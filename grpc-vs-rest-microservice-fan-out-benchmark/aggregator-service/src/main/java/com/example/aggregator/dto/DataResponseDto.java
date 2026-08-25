package com.example.aggregator.dto;

import java.util.List;

public record DataResponseDto(
        String requestId,
        List<DataItemDto> items
) {}
