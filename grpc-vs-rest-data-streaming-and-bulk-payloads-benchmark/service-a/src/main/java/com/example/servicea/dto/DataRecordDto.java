package com.example.servicea.dto;

import lombok.Getter;

@Getter
public class DataRecordDto {
    private final Integer id;
    private final String title;
    private final double amount;
    private final long timestamp;

    public DataRecordDto(Integer id, String title, double amount, long timestamp) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}
