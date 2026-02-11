package com.awscapstone.crypto_tracker_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

@Configuration
public class DynamoDBconfig {

    @Value("${aws.region}")
    private String region;

    @Bean
    public DynamoDbClient dynamoDbClient(){
        return DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public CloudWatchClient cloudWatchClient() {
        return CloudWatchClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Value("${dynamodb.table.users}")
    private String usersTableName;

    @Value("${dynamodb.table.market-prices}")
    private String marketPricesTableName;

    @Value("${dynamodb.table.watchlist}")
    private String watchlistTableName;

    @Bean
    public String usersTableName() {
        return usersTableName;
    }

    @Bean
    public String marketPricesTableName() {
        return marketPricesTableName;
    }

    @Bean
    public String watchlistTableName() {
        return watchlistTableName;
    }
}
