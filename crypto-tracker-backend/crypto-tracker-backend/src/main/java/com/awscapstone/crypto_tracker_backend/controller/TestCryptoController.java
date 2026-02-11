package com.awscapstone.crypto_tracker_backend.controller;

import com.awscapstone.crypto_tracker_backend.service.CoinGeckoService;
import com.awscapstone.crypto_tracker_backend.model.CryptoPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/test/crypto")
@RequiredArgsConstructor
@Slf4j
public class TestCryptoController {

    private final CoinGeckoService coinGeckoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/prices")
    public Map<String, Object> getPrices() {
        log.info("Fetching LIVE crypto prices from CoinGecko");
        
        try {
            // Fetch real data from CoinGecko
            var cryptoPrices = coinGeckoService.fetchCryptoData();
            
            // Convert to frontend-friendly format
            List<Map<String, Object>> formattedData = new ArrayList<>();
            for (CryptoPrice crypto : cryptoPrices) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", crypto.getCoinId());
                item.put("symbol", crypto.getSymbol());
                item.put("name", crypto.getName());
                item.put("image", crypto.getImage());
                item.put("current_price", crypto.getCurrentPrice());
                item.put("market_cap", crypto.getMarketCap());
                item.put("market_cap_rank", crypto.getMarketCapRank());
                item.put("total_volume", crypto.getTotalVolume());
                item.put("price_change_percentage_24h", crypto.getPriceChangePercentage24h());
                item.put("circulating_supply", crypto.getCirculatingSupply());
                item.put("ath", crypto.getAth());
                
                // Parse sparkline data
                if (crypto.getSparkline7d() != null && !crypto.getSparkline7d().isEmpty()) {
                    try {
                        ArrayNode sparklineArray = (ArrayNode) objectMapper.readTree(crypto.getSparkline7d());
                        Map<String, Object> sparklineObj = new HashMap<>();
                        sparklineObj.put("price", sparklineArray);
                        item.put("sparkline_in_7d", sparklineObj);
                    } catch (Exception e) {
                        log.warn("Failed to parse sparkline for {}", crypto.getCoinId());
                        item.put("sparkline_in_7d", Map.of("price", new ArrayList<>()));
                    }
                } else {
                    item.put("sparkline_in_7d", Map.of("price", new ArrayList<>()));
                }
                
                formattedData.add(item);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Live crypto prices fetched successfully from CoinGecko");
            response.put("data", formattedData);
            response.put("count", formattedData.size());
            return response;
            
        } catch (Exception e) {
            log.error("Error fetching from CoinGecko: {}", e.getMessage(), e);
            
            // Fallback to mock data if API fails
            List<Map<String, Object>> mockData = Arrays.asList(
                createMockCrypto("bitcoin", "btc", "Bitcoin", 43250.50, 2.34, 1),
                createMockCrypto("ethereum", "eth", "Ethereum", 2280.75, 1.87, 2),
                createMockCrypto("tether", "usdt", "Tether", 1.00, 0.01, 3),
                createMockCrypto("binancecoin", "bnb", "BNB", 315.20, -0.45, 4),
                createMockCrypto("ripple", "xrp", "XRP", 0.52, 3.21, 5),
                createMockCrypto("usd-coin", "usdc", "USDC", 1.00, 0.00, 6),
                createMockCrypto("cardano", "ada", "Cardano", 0.48, 4.56, 7),
                createMockCrypto("solana", "sol", "Solana", 98.45, 5.67, 8),
                createMockCrypto("dogecoin", "doge", "Dogecoin", 0.085, 2.34, 9),
                createMockCrypto("polkadot", "dot", "Polkadot", 6.75, 1.23, 10)
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Mock crypto prices (CoinGecko API unavailable)");
            response.put("data", mockData);
            response.put("count", mockData.size());
            return response;
        }
    }

    private Map<String, Object> createMockCrypto(String id, String symbol, String name, double price, double change, int rank) {
        Map<String, Object> crypto = new HashMap<>();
        crypto.put("id", id);
        crypto.put("symbol", symbol);
        crypto.put("name", name);
        crypto.put("image", "https://assets.coingecko.com/coins/images/" + rank + "/large/" + id + ".png");
        crypto.put("current_price", price);
        crypto.put("market_cap", price * 19000000);
        crypto.put("market_cap_rank", rank);
        crypto.put("total_volume", price * 5000000);
        crypto.put("price_change_percentage_24h", change);
        crypto.put("circulating_supply", 19000000);
        crypto.put("max_supply", 21000000);
        crypto.put("ath", price * 1.5);
        
        // Generate realistic sparkline data
        List<Double> sparkline = new ArrayList<>();
        Random random = new Random();
        double basePrice = price;
        for (int i = 0; i < 168; i++) { // 7 days * 24 hours
            double variation = (random.nextDouble() - 0.5) * (price * 0.05); // ±5% variation
            sparkline.add(basePrice + variation);
            basePrice = basePrice + (variation * 0.1); // Slight trend
        }
        
        Map<String, Object> sparklineObj = new HashMap<>();
        sparklineObj.put("price", sparkline);
        crypto.put("sparkline_in_7d", sparklineObj);
        
        return crypto;
    }
}
