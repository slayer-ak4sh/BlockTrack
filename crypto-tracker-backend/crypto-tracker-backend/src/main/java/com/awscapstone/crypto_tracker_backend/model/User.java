package com.awscapstone.crypto_tracker_backend.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
public class User {
    private String username;
    private String email;
    private String password;
    private String role = "USER";
    private Long createdAt;

    @DynamoDbPartitionKey
    public String getUsername() {
        return username;
    }
}
