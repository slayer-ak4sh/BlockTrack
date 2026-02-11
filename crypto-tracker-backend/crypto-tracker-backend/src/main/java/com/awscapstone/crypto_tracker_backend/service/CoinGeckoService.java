package com.awscapstone.crypto_tracker_backend.service;

import com.awscapstone.crypto_tracker_backend.model.CryptoPrice;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CoinGeckoService {

    @Value("${coingecko.api.key}")
    private String apiKey;

    @Value("${coingecko.api.url}")
    private String apiUrl;

    @Value("${coingecko.api.vs-currency}")
    private String vsCurrency;

    @Value("${coingecko.api.order}")
    private String order;

    @Value("${coingecko.api.per-page:10}")
    private int perPage;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<CryptoPrice> fetchCryptoData() {
        try {
            String url;
            if (apiKey != null && !apiKey.isEmpty() && !"demo".equals(apiKey)) {
                url = String.format("%s?vs_currency=%s&order=%s&per_page=%d&page=1&sparkline=true&price_change_percentage=1h,24h,7d&x_cg_demo_api_key=%s",
                        apiUrl, vsCurrency, order, perPage, apiKey);
                log.info("Fetching data from CoinGecko with API key");
            } else {
                url = String.format("%s?vs_currency=%s&order=%s&per_page=%d&page=1&sparkline=true&price_change_percentage=1h,24h,7d",
                        apiUrl, vsCurrency, order, perPage);
                log.info("Fetching data from CoinGecko: {}", url);
            }

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            List<CryptoPrice> cryptoPrices = new ArrayList<>();

            for (JsonNode coin : root) {
                CryptoPrice cryptoPrice = new CryptoPrice();

                cryptoPrice.setCoinId(coin.get("id").asText());
                cryptoPrice.setSymbol(coin.get("symbol").asText().toUpperCase());
                cryptoPrice.setName(coin.get("name").asText());
                cryptoPrice.setImage(coin.get("image").asText());
                cryptoPrice.setCurrentPrice(new BigDecimal(coin.get("current_price").asText()));

                if (coin.has("price_change_percentage_1h_in_currency") && !coin.get("price_change_percentage_1h_in_currency").isNull()) {
                    cryptoPrice.setPriceChangePercentage1h(
                            new BigDecimal(coin.get("price_change_percentage_1h_in_currency").asText())
                    );
                } else {
                    cryptoPrice.setPriceChangePercentage1h(BigDecimal.ZERO);
                }

                if (coin.has("price_change_percentage_24h_in_currency") && !coin.get("price_change_percentage_24h_in_currency").isNull()) {
                    cryptoPrice.setPriceChangePercentage24h(
                            new BigDecimal(coin.get("price_change_percentage_24h_in_currency").asText())
                    );
                } else if (coin.has("price_change_percentage_24h") && !coin.get("price_change_percentage_24h").isNull()) {
                    cryptoPrice.setPriceChangePercentage24h(
                            new BigDecimal(coin.get("price_change_percentage_24h").asText())
                    );
                } else {
                    cryptoPrice.setPriceChangePercentage24h(BigDecimal.ZERO);
                }

                if (coin.has("price_change_percentage_7d_in_currency") && !coin.get("price_change_percentage_7d_in_currency").isNull()) {
                    cryptoPrice.setPriceChangePercentage7d(
                            new BigDecimal(coin.get("price_change_percentage_7d_in_currency").asText())
                    );
                } else {
                    cryptoPrice.setPriceChangePercentage7d(BigDecimal.ZERO);
                }

                cryptoPrice.setTotalVolume(new BigDecimal(coin.get("total_volume").asText()));
                cryptoPrice.setMarketCap(new BigDecimal(coin.get("market_cap").asText()));
                cryptoPrice.setMarketCapRank(coin.get("market_cap_rank").asInt());

                // Add circulating and max supply
                if (coin.has("circulating_supply") && !coin.get("circulating_supply").isNull()) {
                    cryptoPrice.setCirculatingSupply(new BigDecimal(coin.get("circulating_supply").asText()));
                }
                if (coin.has("max_supply") && !coin.get("max_supply").isNull()) {
                    cryptoPrice.setMaxSupply(new BigDecimal(coin.get("max_supply").asText()));
                }

                // Convert sparkline data to string
                if (coin.has("sparkline_in_7d") && coin.get("sparkline_in_7d").has("price")) {
                    String sparkline = coin.get("sparkline_in_7d").get("price").toString();
                    cryptoPrice.setSparkline7d(sparkline);
                } else {
                    cryptoPrice.setSparkline7d("[]");
                }

                cryptoPrice.setLastUpdated(LocalDateTime.now());

                cryptoPrices.add(cryptoPrice);
            }

            log.info("Successfully fetched {} cryptocurrencies", cryptoPrices.size());
            return cryptoPrices;

        } catch (Exception e) {
            log.error("Error fetching data from CoinGecko API: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch cryptocurrency data", e);
        }
    }
}
