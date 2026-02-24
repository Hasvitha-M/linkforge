package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsResponse {

    private String shortCode;
    private String originalUrl;
    private Long clickCount;
    private String createdAt;
    private String expiryTime;
    private Boolean isActive;
}