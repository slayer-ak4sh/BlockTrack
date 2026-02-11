package com.awscapstone.crypto_tracker_backend.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CryptoPrice {
    private String coinId;
    private String symbol;
    private String name;
    private String image;
    private BigDecimal currentPrice;
    private BigDecimal priceChangePercentage1h;
    private BigDecimal priceChangePercentage24h;
    private BigDecimal priceChangePercentage7d;
    private BigDecimal totalVolume;
    private BigDecimal marketCap;
    private Integer marketCapRank;
    private BigDecimal circulatingSupply;
    private BigDecimal maxSupply;
    private BigDecimal ath;
    private BigDecimal athChangePercentage;
    private String sparkline7d;
    private LocalDateTime lastUpdated;
}
