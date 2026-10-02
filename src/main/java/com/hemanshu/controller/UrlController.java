package com.hemanshu.controller;

import com.hemanshu.dto.ShortenUrlRequest;
import com.hemanshu.dto.ShortenUrlResponse;
import com.hemanshu.entity.Url;
import com.hemanshu.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/shorten")
    public ShortenUrlResponse shortenUrl(@Valid@RequestBody ShortenUrlRequest request) {

        Url url = urlService.saveUrl(
                request.getOriginalUrl(),
                request.getCustomCode()
        );

        return new ShortenUrlResponse(
                url.getOriginalUrl(),
                url.getShortCode(),
                url.getCreatedAt(),
                url.getExpiresAt(),
                url.getClickCount()
        );
    }

    @DeleteMapping("/api/urls/{shortCode}")
    public ResponseEntity<Void> deleteUrl(
            @PathVariable String shortCode) {

        urlService.deleteUrl(shortCode);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(
            @PathVariable String shortCode) {

        Url url = urlService.getUrlByShortCode(shortCode);

        urlService.incrementClickCount(url);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url.getOriginalUrl()))
                .build();
    }

    @GetMapping("/api/urls/{shortCode}")
    public ShortenUrlResponse getUrlDetails(
            @PathVariable String shortCode) {

        Url url = urlService.getUrlByShortCode(shortCode);

        return new ShortenUrlResponse(
                url.getOriginalUrl(),
                url.getShortCode(),
                url.getCreatedAt(),
                url.getExpiresAt(),
                url.getClickCount()
        );
    }
}