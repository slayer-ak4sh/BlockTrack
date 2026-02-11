package com.awscapstone.crypto_tracker_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchlistService {

    private final DynamoDbClient dynamoDbClient;

    @Qualifier("watchlistTableName")
    private final String watchlistTableName;

    public void addToWatchlist(String userId, String cryptoSymbol) {
        // Check if already in watchlist
        if (isInWatchlist(userId, cryptoSymbol)) {
            throw new RuntimeException("This cryptocurrency is already in your watchlist.");
        }

        try {
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("user_id", AttributeValue.builder().s(userId).build());
            item.put("crypto_symbol", AttributeValue.builder().s(cryptoSymbol.toUpperCase()).build());
            item.put("added_at", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis())).build());

            PutItemRequest request = PutItemRequest.builder()
                    .tableName(watchlistTableName)
                    .item(item)
                    .build();

            dynamoDbClient.putItem(request);
            log.info("Added {} to watchlist for user {}", cryptoSymbol, userId);

        } catch (Exception e) {
            log.error("Error adding to watchlist: {}", e.getMessage());
            throw new RuntimeException("Failed to add to watchlist", e);
        }
    }

    public void removeFromWatchlist(String userId, String cryptoSymbol) {
        try {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("user_id", AttributeValue.builder().s(userId).build());
            key.put("crypto_symbol", AttributeValue.builder().s(cryptoSymbol.toUpperCase()).build());

            DeleteItemRequest request = DeleteItemRequest.builder()
                    .tableName(watchlistTableName)
                    .key(key)
                    .build();

            dynamoDbClient.deleteItem(request);
            log.info("Removed {} from watchlist for user {}", cryptoSymbol, userId);

        } catch (Exception e) {
            log.error("Error removing from watchlist: {}", e.getMessage());
            throw new RuntimeException("Failed to remove from watchlist", e);
        }
    }

    public List<String> getUserWatchlist(String userId) {
        List<String> watchlist = new ArrayList<>();

        try {
            Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
            expressionAttributeValues.put(":user_id", AttributeValue.builder().s(userId).build());

            QueryRequest queryRequest = QueryRequest.builder()
                    .tableName(watchlistTableName)
                    .keyConditionExpression("user_id = :user_id")
                    .expressionAttributeValues(expressionAttributeValues)
                    .build();

            QueryResponse response = dynamoDbClient.query(queryRequest);

            for (Map<String, AttributeValue> item : response.items()) {
                watchlist.add(item.get("crypto_symbol").s());
            }

        } catch (Exception e) {
            log.error("Error fetching watchlist for user {}: {}", userId, e.getMessage());
        }

        return watchlist;
    }

    public boolean isInWatchlist(String userId, String cryptoSymbol) {
        try {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("user_id", AttributeValue.builder().s(userId).build());
            key.put("crypto_symbol", AttributeValue.builder().s(cryptoSymbol.toUpperCase()).build());

            GetItemRequest request = GetItemRequest.builder()
                    .tableName(watchlistTableName)
                    .key(key)
                    .build();

            GetItemResponse response = dynamoDbClient.getItem(request);
            return response.hasItem();

        } catch (Exception e) {
            log.error("Error checking watchlist: {}", e.getMessage());
            return false;
        }
    }
}
