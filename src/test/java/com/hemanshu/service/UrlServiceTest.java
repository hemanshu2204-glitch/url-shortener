package com.hemanshu.service;

import com.hemanshu.entity.Url;
import com.hemanshu.exception.UrlNotFoundException;
import com.hemanshu.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlServiceTest {

    @Test
    void testGenerateShortCode() {

        UrlRepository repository = mock(UrlRepository.class);

        when(repository.findByShortCode(anyString()))
                .thenReturn(java.util.Optional.empty());

        UrlService service = new UrlService(repository);

        String shortCode = service.generateShortCode();

        assertNotNull(shortCode);
        assertEquals(6, shortCode.length());
    }
    @Test
    void testCustomShortCode() {

        UrlRepository repository = mock(UrlRepository.class);

        when(repository.findByShortCode("iit"))
                .thenReturn(java.util.Optional.empty());

        when(repository.save(any(com.hemanshu.entity.Url.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UrlService service = new UrlService(repository);

        com.hemanshu.entity.Url url =
                service.saveUrl("https://iitbhu.ac.in", "iit");

        assertNotNull(url);
        assertEquals("iit", url.getShortCode());
        assertEquals("https://iitbhu.ac.in", url.getOriginalUrl());
    }
    @Test
    void testExpiredUrl() {

        UrlRepository repository = mock(UrlRepository.class);

        Url expiredUrl = new Url();
        expiredUrl.setShortCode("expired");
        expiredUrl.setOriginalUrl("https://example.com");
        expiredUrl.setExpiresAt(LocalDateTime.now().minusMinutes(10));

        when(repository.findByShortCode("expired"))
                .thenReturn(java.util.Optional.of(expiredUrl));

        UrlService service = new UrlService(repository);

        UrlNotFoundException exception = assertThrows(
                UrlNotFoundException.class,
                () -> service.getUrlByShortCode("expired")
        );

        assertEquals(
                "Short URL has expired",
                exception.getMessage()
        );
    }
    @Test
    void testDuplicateCustomShortCode() {

        UrlRepository repository = mock(UrlRepository.class);

        Url existingUrl = new Url();
        existingUrl.setShortCode("iit");

        when(repository.findByShortCode("iit"))
                .thenReturn(java.util.Optional.of(existingUrl));

        UrlService service = new UrlService(repository);

        UrlNotFoundException exception = assertThrows(
                UrlNotFoundException.class,
                () -> service.saveUrl("https://example.com", "iit")
        );

        assertEquals(
                "Custom short code already exists",
                exception.getMessage()
        );
    }

    @Test
    void testIncrementClickCount() {

        UrlRepository repository = mock(UrlRepository.class);

        Url url = new Url();
        url.setShortCode("iit");
        url.setClickCount(0);

        when(repository.save(any(Url.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UrlService service = new UrlService(repository);

        service.incrementClickCount(url);

        assertEquals(1, url.getClickCount());
    }

    @Test
    void testUrlNotFound() {

        UrlRepository repository = mock(UrlRepository.class);

        when(repository.findByShortCode("xyz"))
                .thenReturn(java.util.Optional.empty());

        UrlService service = new UrlService(repository);

        UrlNotFoundException exception = assertThrows(
                UrlNotFoundException.class,
                () -> service.getUrlByShortCode("xyz")
        );

        assertEquals(
                "Short URL not found",
                exception.getMessage()
        );
    }

    @Test
    void testValidUrl() {

        UrlRepository repository = mock(UrlRepository.class);

        Url url = new Url();
        url.setShortCode("iit");
        url.setOriginalUrl("https://iitbhu.ac.in");
        url.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        when(repository.findByShortCode("iit"))
                .thenReturn(java.util.Optional.of(url));

        UrlService service = new UrlService(repository);

        Url result = service.getUrlByShortCode("iit");

        assertNotNull(result);
        assertEquals("iit", result.getShortCode());
        assertEquals("https://iitbhu.ac.in", result.getOriginalUrl());
    }
}