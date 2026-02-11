package com.awscapstone.crypto_tracker_backend.controller;

import com.awscapstone.crypto_tracker_backend.dto.WatchlistRequest;
import com.awscapstone.crypto_tracker_backend.model.CryptoPrice;
import com.awscapstone.crypto_tracker_backend.service.DynamoDBService;
import com.awscapstone.crypto_tracker_backend.service.WatchlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
@Slf4j
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final DynamoDBService dynamoDBService;

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getWatchlist(@PathVariable String userId) {
        try {
            List<String> symbols = watchlistService.getUserWatchlist(userId);
            List<CryptoPrice> cryptoDetails = new ArrayList<>();

            for (String symbol : symbols) {
                CryptoPrice crypto = dynamoDBService.getCryptoPriceBySymbol(symbol);
                if (crypto != null) {
                    cryptoDetails.add(crypto);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", cryptoDetails);
            response.put("count", cryptoDetails.size());
            response.put("userId", userId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error fetching watchlist: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to fetch watchlist");
            errorResponse.put("userId", userId);

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToWatchlist(
            @Valid @RequestBody WatchlistRequest request) {

        try {
            String userId = request.getUserId();
            String symbol = request.getSymbol();

            log.info("Adding {} to watchlist for user {}", symbol, userId);

            watchlistService.addToWatchlist(userId, symbol);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cryptocurrency added to watchlist!");
            response.put("userId", userId);
            response.put("symbol", symbol);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error adding to watchlist: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<Map<String, Object>> removeFromWatchlist(
            @Valid @RequestBody WatchlistRequest request) {

        try {
            String userId = request.getUserId();
            String symbol = request.getSymbol();

            log.info("Removing {} from watchlist for user {}", symbol, userId);

            watchlistService.removeFromWatchlist(userId, symbol);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cryptocurrency removed from watchlist!");
            response.put("userId", userId);
            response.put("symbol", symbol);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error removing from watchlist: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/check/{userId}/{symbol}")
    public ResponseEntity<Map<String, Object>> checkInWatchlist(
            @PathVariable String userId, @PathVariable String symbol) {

        boolean inWatchlist = watchlistService.isInWatchlist(userId, symbol);

        Map<String, Object> response = new HashMap<>();
        response.put("in_watchlist", inWatchlist);
        response.put("user_id", userId);
        response.put("symbol", symbol);
        response.put("message", inWatchlist ?
                "Cryptocurrency is in watchlist" :
                "Cryptocurrency is not in watchlist");

        return ResponseEntity.ok(response);
    }
}