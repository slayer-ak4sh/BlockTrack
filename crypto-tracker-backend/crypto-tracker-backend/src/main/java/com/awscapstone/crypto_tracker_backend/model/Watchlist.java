package com.awscapstone.crypto_tracker_backend.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
public class Watchlist {
    private String username;
    private String coinId;
    private String coinName;
    private String symbol;
    private Long addedAt;

    @DynamoDbPartitionKey
    public String getUsername() {
        return username;
    }

    @DynamoDbSortKey
    public String getCoinId() {
        return coinId;
    }
}