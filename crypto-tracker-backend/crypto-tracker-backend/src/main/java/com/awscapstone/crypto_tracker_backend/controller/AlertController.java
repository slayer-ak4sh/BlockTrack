package com.awscapstone.crypto_tracker_backend.controller;

import com.awscapstone.crypto_tracker_backend.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Slf4j
public class AlertController {

    private final AlertService alertService;

    @PostMapping("/price/{symbol}")
    public ResponseEntity<Map<String, Object>> setPriceAlert(
            @PathVariable String symbol,
            @RequestParam double threshold) {
        
        log.info("Setting price alert for {} at ${}", symbol, threshold);
        
        try {
            alertService.setPriceAlert(symbol, threshold);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", String.format("Price alert set for %s at $%.2f", symbol.toUpperCase(), threshold));
            response.put("symbol", symbol.toUpperCase());
            response.put("threshold", threshold);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error setting price alert: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to set price alert");
            
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @DeleteMapping("/price/{symbol}")
    public ResponseEntity<Map<String, Object>> removePriceAlert(@PathVariable String symbol) {
        log.info("Removing price alert for {}", symbol);
        
        try {
            alertService.removePriceAlert(symbol);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", String.format("Price alert removed for %s", symbol.toUpperCase()));
            response.put("symbol", symbol.toUpperCase());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error removing price alert: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to remove price alert");
            
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/price")
    public ResponseEntity<Map<String, Object>> getAllAlerts() {
        log.info("Fetching all price alerts");
        
        try {
            Map<String, Double> alerts = alertService.getAllAlerts();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("alerts", alerts);
            response.put("count", alerts.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching alerts: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to fetch alerts");
            
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}