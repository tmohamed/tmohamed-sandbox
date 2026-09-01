package com.example.servicea.controller;

import com.example.servicea.dto.DataRecordDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/export")
public class DataControllerA {

    // A. REST NDJSON Streaming (Chunked HTTP)
    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DataRecordDto> streamExport(@RequestParam(defaultValue = "100000") int count) {
        long now = System.currentTimeMillis();
        return Flux.range(1, count)
                .map(i -> new DataRecordDto(i, "Bulk Export Data Record #" + i, 10.50 + i, now));
    }

    // B. REST Paginated Fetch (1,000 per page)
    @GetMapping("/page")
    public List<DataRecordDto> getPage(@RequestParam int page, @RequestParam(defaultValue = "1000") int size) {
        long now = System.currentTimeMillis();
        int startId = (page * size) + 1;
        List<DataRecordDto> pageData = new ArrayList<>(size);

        for (int i = startId; i < startId + size; i++) {
            pageData.add(new DataRecordDto(i, "Bulk Export Data Record #" + i, 10.50 + i, now));
        }

        return pageData;
    }
}
