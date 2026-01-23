package com.codealpha.codealpha_simple_url_shortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortenRequest {

    @NotBlank(message = "URL must not be empty")
    @Size(max = 2048, message = "URL is too long")
    private String url;
}

