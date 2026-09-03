package com.example.servicea.service;

import com.example.servicea.dto.DataItemDto;
import com.example.servicea.dto.DataResponseDto;
import com.example.servicea.grpc.generated.DataItem;
import com.example.servicea.grpc.generated.DataResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataServiceA {
    private final List<DataItemDto> restItems = new ArrayList<>();
    private final List<DataItem> grpcItems = new ArrayList<>();

    public DataServiceA() {
        long now = System.currentTimeMillis();
        for (int i = 1; i <= 100; i++) {
            String id = "A-" + i;
            String name = "Benchmark Product Item #" + i;
            double price = 10.50 + i;
            int quantity = i * 2;

            restItems.add(new DataItemDto(id, name, price, quantity, now));

            grpcItems.add(DataItem.newBuilder()
                    .setId(id)
                    .setName(name)
                    .setPrice(price)
                    .setQuantity(quantity)
                    .setTimestamp(now)
                    .build());
        }
    }

    public DataResponseDto getRestData(String requestId) {
        return new DataResponseDto(requestId, restItems);
    }

    public DataResponse getGrpcData(String requestId) {
        return DataResponse.newBuilder()
                .setRequestId(requestId)
                .addAllItems(grpcItems)
                .build();
    }
}
