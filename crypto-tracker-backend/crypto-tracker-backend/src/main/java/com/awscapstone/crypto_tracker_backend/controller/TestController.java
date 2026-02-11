package com.awscapstone.crypto_tracker_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/crypto/prices")
    public ResponseEntity<List<Map<String, Object>>> getTestCryptoPrices() {
        List<Map<String, Object>> testData = new ArrayList<>();
        
        // Create test crypto data
        Map<String, Object> btc = new HashMap<>();
        btc.put("id", "bitcoin");
        btc.put("symbol", "btc");
        btc.put("name", "Bitcoin");
        btc.put("image", "https://assets.coingecko.com/coins/images/1/large/bitcoin.png");
        btc.put("current_price", 45000.0);
        btc.put("market_cap", 850000000000L);
        btc.put("market_cap_rank", 1);
        btc.put("total_volume", 25000000000L);
        btc.put("high_24h", 46000.0);
        btc.put("low_24h", 44000.0);
        btc.put("price_change_24h", 1000.0);
        btc.put("price_change_percentage_24h", 2.27);
        btc.put("circulating_supply", 19500000.0);
        btc.put("total_supply", 21000000.0);
        btc.put("max_supply", 21000000.0);
        btc.put("ath", 69000.0);
        btc.put("ath_change_percentage", -34.78);
        btc.put("ath_date", "2021-11-10T14:24:11.849Z");
        btc.put("atl", 67.81);
        btc.put("atl_change_percentage", 66251.0);
        btc.put("atl_date", "2013-07-06T00:00:00.000Z");
        btc.put("last_updated", "2024-01-01T12:00:00.000Z");
        
        Map<String, Object> eth = new HashMap<>();
        eth.put("id", "ethereum");
        eth.put("symbol", "eth");
        eth.put("name", "Ethereum");
        eth.put("image", "https://assets.coingecko.com/coins/images/279/large/ethereum.png");
        eth.put("current_price", 2800.0);
        eth.put("market_cap", 340000000000L);
        eth.put("market_cap_rank", 2);
        eth.put("total_volume", 15000000000L);
        eth.put("high_24h", 2850.0);
        eth.put("low_24h", 2750.0);
        eth.put("price_change_24h", 50.0);
        eth.put("price_change_percentage_24h", 1.82);
        eth.put("circulating_supply", 120000000.0);
        eth.put("total_supply", 120000000.0);
        eth.put("ath", 4878.26);
        eth.put("ath_change_percentage", -42.6);
        eth.put("ath_date", "2021-11-10T14:24:19.604Z");
        eth.put("atl", 0.432979);
        eth.put("atl_change_percentage", 646580.0);
        eth.put("atl_date", "2015-10-20T00:00:00.000Z");
        eth.put("last_updated", "2024-01-01T12:00:00.000Z");
        
        testData.add(btc);
        testData.add(eth);
        
        return ResponseEntity.ok(testData);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> testRegister(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        
        user.put("username", request.get("username"));
        user.put("email", request.get("email"));
        user.put("role", "USER");
        
        response.put("success", true);
        response.put("message", "User registered successfully");
        response.put("user", user);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> testLogin(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        
        user.put("username", request.get("username"));
        user.put("email", request.get("username") + "@test.com");
        user.put("role", "USER");
        
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("user", user);
        
        return ResponseEntity.ok(response);
    }
}