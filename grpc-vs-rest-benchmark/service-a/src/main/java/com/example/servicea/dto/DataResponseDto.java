package com.example.servicea.dto;

import java.util.List;

public record DataResponseDto(
        String requestId,
        List<DataItemDto> items
) {}
