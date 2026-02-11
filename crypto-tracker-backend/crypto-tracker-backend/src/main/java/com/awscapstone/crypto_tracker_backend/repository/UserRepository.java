package com.awscapstone.crypto_tracker_backend.repository;

import com.awscapstone.crypto_tracker_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
public class UserRepository {

    private final DynamoDbTable<User> userTable;

    @Autowired
    public UserRepository(DynamoDbEnhancedClient enhancedClient, 
                         @Qualifier("usersTableName") String tableName) {
        this.userTable = enhancedClient.table(tableName, TableSchema.fromBean(User.class));
    }

    public User save(User user) {
        userTable.putItem(user);
        return user;
    }

    public Optional<User> findByUsername(String username) {
        Key key = Key.builder().partitionValue(username).build();
        User user = userTable.getItem(key);
        return Optional.ofNullable(user);
    }

    public void deleteByUsername(String username) {
        Key key = Key.builder().partitionValue(username).build();
        userTable.deleteItem(key);
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }
}