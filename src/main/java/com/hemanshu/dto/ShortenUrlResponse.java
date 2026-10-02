package com.hemanshu.dto;

import java.time.LocalDateTime;

public class ShortenUrlResponse {

    private String originalUrl;
    private String shortCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private long clickCount;

    public ShortenUrlResponse(
            String originalUrl,
            String shortCode,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            long clickCount) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
        this.expiresAt=expiresAt;
        this.clickCount=clickCount;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public String getShortCode() {
        return shortCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getClickCount() {
        return clickCount;
    }
}