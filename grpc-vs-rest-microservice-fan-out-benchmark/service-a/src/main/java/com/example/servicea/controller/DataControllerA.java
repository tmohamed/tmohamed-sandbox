package com.example.servicea.controller;

import com.example.servicea.dto.DataResponseDto;
import com.example.servicea.service.DataServiceA;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/data")
public class DataControllerA {
    private final DataServiceA mockService;

    public DataControllerA(DataServiceA mockService) {
        this.mockService = mockService;
    }

    @GetMapping("/{requestId}")
    public DataResponseDto getData(@PathVariable String requestId) {
        return mockService.getRestData(requestId);
    }
}
