package com.awscapstone.crypto_tracker_backend.service;

import com.awscapstone.crypto_tracker_backend.model.CryptoPrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final CloudWatchService cloudWatchService;
    private final Map<String, Double> priceThresholds = new ConcurrentHashMap<>();
    private final Map<String, BigDecimal> lastPrices = new ConcurrentHashMap<>();

    public void setPriceAlert(String symbol, double threshold) {
        priceThresholds.put(symbol.toLowerCase(), threshold);
        log.info("Price alert set for {}: ${}", symbol, threshold);
    }

    public void removePriceAlert(String symbol) {
        priceThresholds.remove(symbol.toLowerCase());
        log.info("Price alert removed for {}", symbol);
    }

    public void checkPriceAlerts(CryptoPrice cryptoPrice) {
        String symbol = cryptoPrice.getSymbol().toLowerCase();
        BigDecimal currentPrice = cryptoPrice.getCurrentPrice();
        
        Double threshold = priceThresholds.get(symbol);
        BigDecimal lastPrice = lastPrices.get(symbol);
        
        if (threshold != null && currentPrice != null) {
            double currentPriceDouble = currentPrice.doubleValue();
            
            // Check if price crossed threshold
            if (currentPriceDouble <= threshold && (lastPrice == null || lastPrice.doubleValue() > threshold)) {
                triggerPriceAlert(cryptoPrice, threshold, "BELOW");
            } else if (currentPriceDouble >= threshold && (lastPrice == null || lastPrice.doubleValue() < threshold)) {
                triggerPriceAlert(cryptoPrice, threshold, "ABOVE");
            }
        }
        
        if (currentPrice != null) {
            lastPrices.put(symbol, currentPrice);
        }
    }

    private void triggerPriceAlert(CryptoPrice cryptoPrice, double threshold, String direction) {
        String alertMessage = String.format(
            "PRICE ALERT: %s is now %s $%.2f (threshold: $%.2f)",
            cryptoPrice.getSymbol().toUpperCase(),
            direction,
            cryptoPrice.getCurrentPrice().doubleValue(),
            threshold
        );
        
        log.warn(alertMessage);
        
        // Publish alert metric to CloudWatch
        cloudWatchService.publishMetric("PriceAlert", 1, "Count");
        
        // In a real application, you would send notifications here
        sendNotification(alertMessage);
    }

    private void sendNotification(String message) {
        // Placeholder for notification logic
        log.info("NOTIFICATION: {}", message);
    }

    public Map<String, Double> getAllAlerts() {
        return Map.copyOf(priceThresholds);
    }
}