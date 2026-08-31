package com.example.servicea.controller;

import com.example.servicea.dto.DataDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataControllerA {

    @GetMapping("/api/slow-data/{id}")
    public DataDto getSlowData(@PathVariable String id) throws InterruptedException {
        System.out.println("[REST Server] Started processing slow query for ID: " + id);

        for (int i = 1; i <= 50; i++) {
            Thread.sleep(100); // 5-second delay
        }

        System.out.println(">>> [REST Server] FINISHED 5-second task completely (CPU cycles wasted even if client left).");
        return new DataDto(id, "Slow query completed", System.currentTimeMillis());
    }
}
