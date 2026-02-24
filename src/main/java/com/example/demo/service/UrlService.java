package com.example.demo.service;

import com.example.demo.dto.AnalyticsResponse;
import com.example.demo.dto.ShortenRequest;
import com.example.demo.dto.ShortenResponse;

public interface UrlService {

    ShortenResponse createShortUrl(ShortenRequest request);
    AnalyticsResponse getAnalytics(String shortCode);
    String getOriginalUrl(String shortCode);

    Long getClickCount(String shortCode);
}
