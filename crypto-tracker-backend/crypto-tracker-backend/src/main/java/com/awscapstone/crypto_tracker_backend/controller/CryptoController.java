package com.awscapstone.crypto_tracker_backend.controller;

import com.awscapstone.crypto_tracker_backend.model.CryptoPrice;
import com.awscapstone.crypto_tracker_backend.service.CoinGeckoService;
import com.awscapstone.crypto_tracker_backend.service.DynamoDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
@Slf4j
public class CryptoController {

    private final CoinGeckoService coinGeckoService;
    private final DynamoDBService dynamoDBService;

    @GetMapping("/prices")
    public ResponseEntity<List<CryptoPrice>> getCryptoData() {
        log.info("Fetching cryptocurrency data");

        try {
            // First try to get stored data
            List<CryptoPrice> storedData = dynamoDBService.getAllCryptoPrices();
            
            if (!storedData.isEmpty()) {
                log.info("Returning {} stored cryptocurrencies", storedData.size());
                return ResponseEntity.ok(storedData);
            }
            
            // If no stored data, fetch fresh data
            List<CryptoPrice> cryptoPrices = coinGeckoService.fetchCryptoData();
            dynamoDBService.storeCryptoData(cryptoPrices);
            return ResponseEntity.ok(cryptoPrices);

        } catch (Exception e) {
            log.error("Error in /prices endpoint: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/prices/stored")
    public ResponseEntity<Map<String, Object>> getStoredCryptoData() {
        log.info("Fetching stored cryptocurrency data from DynamoDB");

        try {
            List<CryptoPrice> cryptoPrices = dynamoDBService.getAllCryptoPrices();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", cryptoPrices);
            response.put("count", cryptoPrices.size());
            response.put("message", "Stored cryptocurrency data retrieved successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error fetching stored crypto data: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to fetch stored cryptocurrency data");

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/price/{symbol}")
    public ResponseEntity<Map<String, Object>> getCryptoBySymbol(@PathVariable String symbol) {
        log.info("Fetching crypto data for symbol: {}", symbol);

        try {
            CryptoPrice cryptoPrice = dynamoDBService.getCryptoPriceBySymbol(symbol);

            if (cryptoPrice != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", cryptoPrice);
                response.put("message", "Cryptocurrency data retrieved successfully");

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Cryptocurrency not found for symbol: " + symbol);

                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            log.error("Error fetching crypto by symbol {}: {}", symbol, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to fetch cryptocurrency data");

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshPrices() {
        log.info("Manually refreshing cryptocurrency prices");

        try {
            List<CryptoPrice> cryptoPrices = coinGeckoService.fetchCryptoData();
            dynamoDBService.storeCryptoData(cryptoPrices);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Prices updated successfully!");
            response.put("count", cryptoPrices.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error refreshing prices: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to refresh prices");

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
