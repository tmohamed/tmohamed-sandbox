package com.example.serviceb.dto;

import java.util.List;

public record DataResponseDto(
        String requestId,
        List<DataItemDto> items
) {}
