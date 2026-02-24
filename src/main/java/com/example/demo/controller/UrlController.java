package com.example.demo.controller;
import com.example.demo.dto.AnalyticsResponse;
import com.example.demo.dto.ShortenRequest;
import com.example.demo.dto.ShortenResponse;
import com.example.demo.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/shorten")
    public ShortenResponse shorten(@Valid @RequestBody ShortenRequest request) {
        return urlService.createShortUrl(request);
    }

    @GetMapping("/analytics/{code}")
    public ResponseEntity<AnalyticsResponse> getAnalytics(
            @PathVariable String code) {
        return ResponseEntity.ok(urlService.getAnalytics(code));
    }
}
