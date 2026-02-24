package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShortenRequest {

    @NotBlank
    private String longUrl;

    private String customAlias;

    private Long expiryMinutes;
}
