package com.awscapstone.crypto_tracker_backend.service;

import com.awscapstone.crypto_tracker_backend.model.CryptoPrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DynamoDBService {

    private final DynamoDbClient dynamoDbClient;

    @Qualifier("marketPricesTableName")
    private final String marketPricesTableName;

    @Qualifier("usersTableName")
    private final String usersTableName;

    @Qualifier("watchlistTableName")
    private final String watchlistTableName;

    public void storeCryptoData(List<CryptoPrice> cryptoPrices) {
        for (CryptoPrice crypto : cryptoPrices) {
            try {
                Map<String, AttributeValue> item = new HashMap<>();
                item.put("symbol", AttributeValue.builder().s(crypto.getSymbol()).build());
                item.put("timestamp", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis())).build());
                item.put("coinId", AttributeValue.builder().s(crypto.getCoinId() != null ? crypto.getCoinId() : crypto.getSymbol().toLowerCase()).build());
                item.put("name", AttributeValue.builder().s(crypto.getName() != null ? crypto.getName() : crypto.getSymbol()).build());
                item.put("image", AttributeValue.builder().s(crypto.getImage() != null ? crypto.getImage() : "").build());
                item.put("current_price", AttributeValue.builder().n(crypto.getCurrentPrice().toString()).build());
                item.put("price_change_percentage_1h", AttributeValue.builder().n(crypto.getPriceChangePercentage1h().toString()).build());
                item.put("price_change_percentage_24h", AttributeValue.builder().n(crypto.getPriceChangePercentage24h().toString()).build());
                item.put("price_change_percentage_7d", AttributeValue.builder().n(crypto.getPriceChangePercentage7d().toString()).build());
                item.put("total_volume", AttributeValue.builder().n(crypto.getTotalVolume().toString()).build());
                item.put("market_cap", AttributeValue.builder().n(crypto.getMarketCap().toString()).build());
                item.put("market_cap_rank", AttributeValue.builder().n(String.valueOf(crypto.getMarketCapRank())).build());
                item.put("sparkline_7d", AttributeValue.builder().s(crypto.getSparkline7d() != null ? crypto.getSparkline7d() : "[]").build());
                item.put("last_updated", AttributeValue.builder().s(crypto.getLastUpdated().toString()).build());

                PutItemRequest request = PutItemRequest.builder()
                        .tableName(marketPricesTableName)
                        .item(item)
                        .build();

                dynamoDbClient.putItem(request);
                log.debug("Stored {} in DynamoDB", crypto.getSymbol());

            } catch (Exception e) {
                log.error("Error storing {} in DynamoDB: {}", crypto.getSymbol(), e.getMessage());
            }
        }
        log.info("Stored {} cryptocurrencies in DynamoDB", cryptoPrices.size());
    }

    public List<CryptoPrice> getAllCryptoPrices() {
        List<CryptoPrice> result = new ArrayList<>();

        try {
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(marketPricesTableName)
                    .build();

            ScanResponse response = dynamoDbClient.scan(scanRequest);

            for (Map<String, AttributeValue> item : response.items()) {
                CryptoPrice cryptoPrice = mapToCryptoPrice(item);
                result.add(cryptoPrice);
            }

        } catch (Exception e) {
            log.error("Error scanning DynamoDB table: {}", e.getMessage());
        }

        return result;
    }

    public CryptoPrice getCryptoPriceBySymbol(String symbol) {
        try {
            // Query for the latest entry for this symbol
            Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
            expressionAttributeValues.put(":symbol", AttributeValue.builder().s(symbol.toUpperCase()).build());

            QueryRequest request = QueryRequest.builder()
                    .tableName(marketPricesTableName)
                    .keyConditionExpression("symbol = :symbol")
                    .expressionAttributeValues(expressionAttributeValues)
                    .scanIndexForward(false) // Get latest first
                    .limit(1)
                    .build();

            QueryResponse response = dynamoDbClient.query(request);

            if (!response.items().isEmpty()) {
                return mapToCryptoPrice(response.items().get(0));
            }

        } catch (Exception e) {
            log.error("Error getting crypto price for {}: {}", symbol, e.getMessage());
        }

        return null;
    }

    private CryptoPrice mapToCryptoPrice(Map<String, AttributeValue> item) {
        CryptoPrice cryptoPrice = new CryptoPrice();

        cryptoPrice.setSymbol(item.get("symbol").s());
        if (item.containsKey("coinId")) {
            cryptoPrice.setCoinId(item.get("coinId").s());
        }
        if (item.containsKey("name")) {
            cryptoPrice.setName(item.get("name").s());
        }
        if (item.containsKey("image")) {
            cryptoPrice.setImage(item.get("image").s());
        }
        cryptoPrice.setCurrentPrice(new BigDecimal(item.get("current_price").n()));
        cryptoPrice.setPriceChangePercentage1h(new BigDecimal(item.get("price_change_percentage_1h").n()));
        cryptoPrice.setPriceChangePercentage24h(new BigDecimal(item.get("price_change_percentage_24h").n()));
        cryptoPrice.setPriceChangePercentage7d(new BigDecimal(item.get("price_change_percentage_7d").n()));
        cryptoPrice.setTotalVolume(new BigDecimal(item.get("total_volume").n()));
        cryptoPrice.setMarketCap(new BigDecimal(item.get("market_cap").n()));
        cryptoPrice.setMarketCapRank(Integer.parseInt(item.get("market_cap_rank").n()));
        if (item.containsKey("sparkline_7d")) {
            cryptoPrice.setSparkline7d(item.get("sparkline_7d").s());
        }

        return cryptoPrice;
    }
}
