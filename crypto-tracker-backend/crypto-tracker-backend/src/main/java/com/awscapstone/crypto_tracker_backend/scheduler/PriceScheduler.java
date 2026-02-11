package com.awscapstone.crypto_tracker_backend.scheduler;

import com.awscapstone.crypto_tracker_backend.service.AlertService;
import com.awscapstone.crypto_tracker_backend.service.CloudWatchService;
import com.awscapstone.crypto_tracker_backend.service.CoinGeckoService;
import com.awscapstone.crypto_tracker_backend.service.DynamoDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PriceScheduler {

    private final CoinGeckoService coinGeckoService;
    private final DynamoDBService dynamoDBService;
    private final CloudWatchService cloudWatchService;
    private final AlertService alertService;

    // Fetch prices every 30 seconds
    @Scheduled(fixedRate = 30000)
    public void fetchAndStoreCryptoPrices() {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        
        try {
            log.info("Scheduled task: Fetching cryptocurrency prices...");

            var cryptoPrices = coinGeckoService.fetchCryptoData();
            dynamoDBService.storeCryptoData(cryptoPrices);
            
            // Check price alerts
            cryptoPrices.forEach(alertService::checkPriceAlerts);
            
            success = true;
            log.info("Scheduled task completed: Fetched {} cryptocurrencies", cryptoPrices.size());
            
            // Publish metrics
            cloudWatchService.publishMetric("ScheduledUpdateCount", 1, "Count");
            cloudWatchService.publishMetric("CryptocurrenciesUpdated", cryptoPrices.size(), "Count");

        } catch (Exception e) {
            log.error("Error in scheduled price fetch: {}", e.getMessage());
            cloudWatchService.publishMetric("ScheduledUpdateErrors", 1, "Count");
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            cloudWatchService.publishMetric("ScheduledUpdateDuration", duration, "Milliseconds");
            cloudWatchService.publishMetric("ScheduledUpdateSuccess", success ? 1 : 0, "Count");
        }
    }

    // Clean old data every hour (optional)
    @Scheduled(fixedRate = 3600000)
    public void cleanOldData() {
        log.info("Cleaning old data...");
        // Implement data cleanup logic if needed
    }
}
