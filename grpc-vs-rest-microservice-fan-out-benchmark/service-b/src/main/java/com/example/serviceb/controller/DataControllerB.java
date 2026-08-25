package com.example.serviceb.controller;

import com.example.serviceb.dto.DataResponseDto;
import com.example.serviceb.service.DataServiceB;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/data")
public class DataControllerB {
    private final DataServiceB mockService;

    public DataControllerB(DataServiceB mockService) {
        this.mockService = mockService;
    }

    @GetMapping("/{requestId}")
    public DataResponseDto getData(@PathVariable String requestId) {
        return mockService.getRestData(requestId);
    }
}
