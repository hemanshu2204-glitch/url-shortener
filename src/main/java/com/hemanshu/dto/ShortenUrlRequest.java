package com.hemanshu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ShortenUrlRequest {

    @NotBlank(message = "Original URL cannot be empty")
    @Pattern(
            regexp = "^(https?://).+",
            message = "Original URL must start with http:// or https://"
    )
    private String originalUrl;

    private String customCode;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }
}