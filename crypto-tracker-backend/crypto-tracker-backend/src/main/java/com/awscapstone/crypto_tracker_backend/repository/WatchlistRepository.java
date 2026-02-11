package com.awscapstone.crypto_tracker_backend.repository;

import com.awscapstone.crypto_tracker_backend.model.Watchlist;
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
public class WatchlistRepository {

    private final DynamoDbTable<Watchlist> watchlistTable;

    @Autowired
    public WatchlistRepository(DynamoDbEnhancedClient enhancedClient,
                              @Qualifier("watchlistTableName") String tableName) {
        this.watchlistTable = enhancedClient.table(tableName, TableSchema.fromBean(Watchlist.class));
    }

    public Watchlist save(Watchlist watchlist) {
        watchlistTable.putItem(watchlist);
        return watchlist;
    }

    public List<Watchlist> findByUsername(String username) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(username).build());
        
        return watchlistTable.query(queryConditional)
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public Optional<Watchlist> findByUsernameAndCoinId(String username, String coinId) {
        Key key = Key.builder()
                .partitionValue(username)
                .sortValue(coinId)
                .build();
        Watchlist watchlist = watchlistTable.getItem(key);
        return Optional.ofNullable(watchlist);
    }

    public void deleteByUsernameAndCoinId(String username, String coinId) {
        Key key = Key.builder()
                .partitionValue(username)
                .sortValue(coinId)
                .build();
        watchlistTable.deleteItem(key);
    }

    public boolean existsByUsernameAndCoinId(String username, String coinId) {
        return findByUsernameAndCoinId(username, coinId).isPresent();
    }
}