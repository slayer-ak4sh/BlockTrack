package com.awscapstone.crypto_tracker_backend.runner;

import com.awscapstone.crypto_tracker_backend.service.CoinGeckoService;
import com.awscapstone.crypto_tracker_backend.service.DynamoDBService;
import com.awscapstone.crypto_tracker_backend.service.TableInitializationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppStartupRunner implements ApplicationRunner {

    private final CoinGeckoService coinGeckoService;
    private final DynamoDBService dynamoDBService;
    private final TableInitializationService tableInitializationService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Application starting up...");

        try {
            // Create tables first
            log.info("Creating DynamoDB tables if they don't exist...");
            tableInitializationService.createTablesIfNotExist();
            
            // Wait a bit for tables to be ready
            Thread.sleep(2000);
            
            // Fetch initial data on startup
            log.info("Fetching initial cryptocurrency data...");
            var initialData = coinGeckoService.fetchCryptoData();
            dynamoDBService.storeCryptoData(initialData);

            log.info("Initial data loaded: {} cryptocurrencies", initialData.size());

        } catch (Exception e) {
            log.warn("Could not fetch initial data: {}", e.getMessage());
        }

        log.info("=== Crypto Tracker Backend Ready ===");
        log.info("API Endpoints:");
        log.info("  GET  /health - Health check");
        log.info("  GET  /api/crypto/prices - Fetch latest prices");
        log.info("  GET  /api/crypto/prices/stored - Get stored prices");
        log.info("  POST /api/auth/register - Register user");
        log.info("  POST /api/auth/login - Login user");
        log.info("  GET  /api/watchlist/{userId} - Get user watchlist");
    }
}
