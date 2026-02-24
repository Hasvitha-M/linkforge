package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortenResponse {
    private String shortUrl;
    private String originalUrl;
    private String createdAt;
    private String expiryTime;
    private String qrCodeBase64;
}
