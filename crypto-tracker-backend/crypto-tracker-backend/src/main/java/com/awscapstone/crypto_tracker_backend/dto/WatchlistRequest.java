package com.awscapstone.crypto_tracker_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class WatchlistRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Cryptocurrency symbol is required")
    private String symbol;
}
