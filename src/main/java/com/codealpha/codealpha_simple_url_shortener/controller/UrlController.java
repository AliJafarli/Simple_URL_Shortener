package com.codealpha.codealpha_simple_url_shortener.controller;

import com.codealpha.codealpha_simple_url_shortener.dto.ShortenRequest;
import com.codealpha.codealpha_simple_url_shortener.dto.ShortenResponse;
import com.codealpha.codealpha_simple_url_shortener.entity.UrlMapping;
import com.codealpha.codealpha_simple_url_shortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlShortenerService service;

    @PostMapping("/api/shorten")
    public ResponseEntity<ShortenResponse> shorten(
            @Valid @RequestBody ShortenRequest request,
            HttpServletRequest httpRequest
    ) {
        UrlMapping mapping = service.shorten(request.getUrl());

        String shortUrl = getBaseUrl(httpRequest) + "/" + mapping.getShortCode();

        return ResponseEntity.ok(
                new ShortenResponse(shortUrl, mapping.getOriginalUrl(), mapping.getShortCode())
        );
    }

//    @GetMapping("/{code}")
//    public ResponseEntity<Void> redirect(@PathVariable String code) {
//        UrlMapping mapping = service.findByCode(code);
//        if (mapping == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.status(302)
//                .location(URI.create(mapping.getOriginalUrl()))
//                .build();
//    }

    @GetMapping("/{code:[a-zA-Z0-9]{7}}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        UrlMapping mapping = service.findByCode(code);
        if (mapping == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(302)
                .location(URI.create(mapping.getOriginalUrl()))
                .build();
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();

        boolean defaultPort =
                (scheme.equals("http") && port == 80) ||
                        (scheme.equals("https") && port == 443);

        return defaultPort
                ? scheme + "://" + host
                : scheme + "://" + host + ":" + port;
    }
}
