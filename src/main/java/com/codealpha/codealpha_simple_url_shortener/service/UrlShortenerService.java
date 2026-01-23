package com.codealpha.codealpha_simple_url_shortener.service;

import com.codealpha.codealpha_simple_url_shortener.entity.UrlMapping;
import com.codealpha.codealpha_simple_url_shortener.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.security.SecureRandom;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private static final String SYMBOLS =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final SecureRandom RANDOM = new SecureRandom();

    private final UrlMappingRepository repository;

    public UrlMapping shorten(String originalUrl) {
        validateUrl(originalUrl);

        String code = generateUniqueCode(7);

        UrlMapping mapping = UrlMapping.builder()
                .shortCode(code)
                .originalUrl(originalUrl)
                .createdAt(Instant.now())
                .build();

        return repository.save(mapping);
    }

    public UrlMapping findByCode(String code) {
        return repository.findByShortCode(code).orElse(null);
    }

    private String generateUniqueCode(int length) {
        for (int i = 0; i < 30; i++) {
            String code = randomString(length);
            if (!repository.existsByShortCode(code)) {
                return code;
            }
        }
        return randomString(length + 2);
    }

    private String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length())));
        }
        return sb.toString();
    }

    private void validateUrl(String url) {
        try {
            URI uri = URI.create(url.trim());
            if (uri.getScheme() == null || uri.getHost() == null) {
                throw new IllegalArgumentException("Invalid URL format");
            }
            if (!uri.getScheme().equalsIgnoreCase("http")
                    && !uri.getScheme().equalsIgnoreCase("https")) {
                throw new IllegalArgumentException("Only http/https allowed");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }
}
