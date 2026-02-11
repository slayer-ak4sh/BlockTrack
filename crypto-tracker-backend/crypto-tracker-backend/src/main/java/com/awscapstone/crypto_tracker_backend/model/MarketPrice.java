package com.awscapstone.crypto_tracker_backend.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
public class MarketPrice {
    private String coinId;
    private Long timestamp;
    private String symbol;
    private String name;
    private Double currentPrice;
    private Double marketCap;
    private Double volume24h;
    private Double priceChange24h;
    private Double priceChangePercentage24h;

    @DynamoDbPartitionKey
    public String getCoinId() {
        return coinId;
    }

    @DynamoDbSortKey
    public Long getTimestamp() {
        return timestamp;
    }
}