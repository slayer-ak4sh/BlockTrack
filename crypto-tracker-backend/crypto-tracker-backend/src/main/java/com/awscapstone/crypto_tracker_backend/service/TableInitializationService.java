package com.awscapstone.crypto_tracker_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TableInitializationService {

    private final DynamoDbClient dynamoDbClient;

    @Qualifier("marketPricesTableName")
    private final String marketPricesTableName;

    @Qualifier("usersTableName")
    private final String usersTableName;

    @Qualifier("watchlistTableName")
    private final String watchlistTableName;

    public void createTablesIfNotExist() {
        createMarketPricesTable();
        createUsersTable();
        createWatchlistTable();
    }

    private void createMarketPricesTable() {
        try {
            DescribeTableRequest request = DescribeTableRequest.builder()
                    .tableName(marketPricesTableName)
                    .build();
            dynamoDbClient.describeTable(request);
            log.info("Table {} already exists", marketPricesTableName);
        } catch (ResourceNotFoundException e) {
            log.info("Creating table: {}", marketPricesTableName);
            CreateTableRequest createRequest = CreateTableRequest.builder()
                    .tableName(marketPricesTableName)
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("coinId")
                                    .keyType(KeyType.HASH)
                                    .build(),
                            KeySchemaElement.builder()
                                    .attributeName("timestamp")
                                    .keyType(KeyType.RANGE)
                                    .build()
                    )
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("coinId")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("timestamp")
                                    .attributeType(ScalarAttributeType.N)
                                    .build()
                    )
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .build();

            dynamoDbClient.createTable(createRequest);
            log.info("Table {} created successfully", marketPricesTableName);
        }
    }

    private void createUsersTable() {
        try {
            DescribeTableRequest request = DescribeTableRequest.builder()
                    .tableName(usersTableName)
                    .build();
            dynamoDbClient.describeTable(request);
            log.info("Table {} already exists", usersTableName);
        } catch (ResourceNotFoundException e) {
            log.info("Creating table: {}", usersTableName);
            CreateTableRequest createRequest = CreateTableRequest.builder()
                    .tableName(usersTableName)
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("username")
                                    .keyType(KeyType.HASH)
                                    .build()
                    )
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("username")
                                    .attributeType(ScalarAttributeType.S)
                                    .build()
                    )
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .build();

            dynamoDbClient.createTable(createRequest);
            log.info("Table {} created successfully", usersTableName);
        }
    }

    private void createWatchlistTable() {
        try {
            DescribeTableRequest request = DescribeTableRequest.builder()
                    .tableName(watchlistTableName)
                    .build();
            dynamoDbClient.describeTable(request);
            log.info("Table {} already exists", watchlistTableName);
        } catch (ResourceNotFoundException e) {
            log.info("Creating table: {}", watchlistTableName);
            CreateTableRequest createRequest = CreateTableRequest.builder()
                    .tableName(watchlistTableName)
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("userId")
                                    .keyType(KeyType.HASH)
                                    .build(),
                            KeySchemaElement.builder()
                                    .attributeName("coinId")
                                    .keyType(KeyType.RANGE)
                                    .build()
                    )
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("userId")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("coinId")
                                    .attributeType(ScalarAttributeType.S)
                                    .build()
                    )
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .build();

            dynamoDbClient.createTable(createRequest);
            log.info("Table {} created successfully", watchlistTableName);
        }
    }
}