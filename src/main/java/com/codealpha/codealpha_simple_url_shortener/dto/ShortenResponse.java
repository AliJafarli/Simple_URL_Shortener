package com.codealpha.codealpha_simple_url_shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShortenResponse {
    private String shortUrl;
    private String originalUrl;
    private String code;
}

