package com.example.servicea.dto;

public class DataDto {
    private final String id;
    private final String payload;
    private final Long timestamp;

    public DataDto(String id, String payload, long timestamp) {
        this.id = id;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getPayload() {
        return payload;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
