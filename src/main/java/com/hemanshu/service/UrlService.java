package com.hemanshu.service;

import com.hemanshu.entity.Url;
import com.hemanshu.repository.UrlRepository;
import org.springframework.stereotype.Service;
import com.hemanshu.exception.UrlNotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String generateShortCode() {

        String shortCode;

        do {
            shortCode = UUID.randomUUID()
                    .toString()
                    .substring(0, 6);

        } while (urlRepository.findByShortCode(shortCode).isPresent());

        return shortCode;
    }

    public Url saveUrl(String originalUrl, String customCode) {

        String shortCode;

        if (customCode != null && !customCode.isBlank()) {

            if (urlRepository.findByShortCode(customCode).isPresent()) {
                throw new UrlNotFoundException("Custom short code already exists");
            }

            shortCode = customCode;

        } else {
            shortCode = generateShortCode();
        }

        Url url = new Url();

        LocalDateTime now = LocalDateTime.now();

        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);
        url.setCreatedAt(now);
        url.setExpiresAt(now.plusHours(24));

        return urlRepository.save(url);
    }

    public void deleteUrl(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("Short URL not found"));

        urlRepository.delete(url);
    }

    public Url getUrlByShortCode(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("Short URL not found"));

        if (url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UrlNotFoundException("Short URL has expired");
        }

        return url;
    }

    public void incrementClickCount(Url url) {

        url.setClickCount(url.getClickCount() + 1);

        urlRepository.save(url);
    }
}