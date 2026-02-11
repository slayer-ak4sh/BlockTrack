package com.awscapstone.crypto_tracker_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistResponse {
    private String userId;
    private List<CryptoDetail> cryptocurrencies;
    private int count;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CryptoDetail {
        private String symbol;
        private String name;
        private double currentPrice;
        private double priceChange24h;
        private double marketCap;
        private String sparklineData;
    }
}