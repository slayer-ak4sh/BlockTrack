package com.awscapstone.crypto_tracker_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Service
public class TableCreationService {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    @Qualifier("usersTableName")
    private String usersTableName;

    @Autowired
    @Qualifier("marketPricesTableName")
    private String marketPricesTableName;

    @Autowired
    @Qualifier("watchlistTableName")
    private String watchlistTableName;

    public void createTablesIfNotExist() {
        createUsersTable();
        createMarketPricesTable();
        createWatchlistTable();
    }

    private void createUsersTable() {
        if (!tableExists(usersTableName)) {
            CreateTableRequest request = CreateTableRequest.builder()
                    .tableName(usersTableName)
                    .keySchema(KeySchemaElement.builder()
                            .attributeName("username")
                            .keyType(KeyType.HASH)
                            .build())
                    .attributeDefinitions(AttributeDefinition.builder()
                            .attributeName("username")
                            .attributeType(ScalarAttributeType.S)
                            .build())
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .build();
            
            dynamoDbClient.createTable(request);
            System.out.println("Created table: " + usersTableName);
        }
    }

    private void createMarketPricesTable() {
        if (!tableExists(marketPricesTableName)) {
            CreateTableRequest request = CreateTableRequest.builder()
                    .tableName(marketPricesTableName)
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("symbol")
                                    .keyType(KeyType.HASH)
                                    .build(),
                            KeySchemaElement.builder()
                                    .attributeName("timestamp")
                                    .keyType(KeyType.RANGE)
                                    .build())
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("symbol")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("timestamp")
                                    .attributeType(ScalarAttributeType.N)
                                    .build())
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .build();
            
            dynamoDbClient.createTable(request);
            System.out.println("Created table: " + marketPricesTableName);
        }
    }

    private void createWatchlistTable() {
        if (!tableExists(watchlistTableName)) {
            CreateTableRequest request = CreateTableRequest.builder()
                    .tableName(watchlistTableName)
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("username")
                                    .keyType(KeyType.HASH)
                                    .build(),
                            KeySchemaElement.builder()
                                    .attributeName("symbol")
                                    .keyType(KeyType.RANGE)
                                    .build())
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("username")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("symbol")
                                    .attributeType(ScalarAttributeType.S)
                                    .build())
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .build();
            
            dynamoDbClient.createTable(request);
            System.out.println("Created table: " + watchlistTableName);
        }
    }

    private boolean tableExists(String tableName) {
        try {
            DescribeTableRequest request = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();
            dynamoDbClient.describeTable(request);
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }
}