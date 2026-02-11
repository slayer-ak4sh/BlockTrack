package com.awscapstone.crypto_tracker_backend.repository;

import com.awscapstone.crypto_tracker_backend.model.MarketPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MarketPriceRepository {

    private final DynamoDbTable<MarketPrice> marketPriceTable;

    @Autowired
    public MarketPriceRepository(DynamoDbEnhancedClient enhancedClient,
                                @Qualifier("marketPricesTableName") String tableName) {
        this.marketPriceTable = enhancedClient.table(tableName, TableSchema.fromBean(MarketPrice.class));
    }

    public MarketPrice save(MarketPrice marketPrice) {
        marketPriceTable.putItem(marketPrice);
        return marketPrice;
    }

    public Optional<MarketPrice> findByCoinIdAndTimestamp(String coinId, Long timestamp) {
        Key key = Key.builder()
                .partitionValue(coinId)
                .sortValue(timestamp)
                .build();
        MarketPrice price = marketPriceTable.getItem(key);
        return Optional.ofNullable(price);
    }

    public List<MarketPrice> findByCoinId(String coinId) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(coinId).build());
        
        return marketPriceTable.query(queryConditional)
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public List<MarketPrice> findLatestPrices() {
        return marketPriceTable.scan()
                .items()
                .stream()
                .collect(Collectors.toList());
    }
}