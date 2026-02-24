package com.example.demo.service;

import com.example.demo.dto.AnalyticsResponse;
import com.example.demo.dto.ShortenRequest;
import com.example.demo.dto.ShortenResponse;
import com.example.demo.entity.UrlMapping;
import com.example.demo.repository.UrlMappingRepository;
import com.example.demo.util.Base62Encoder;
import com.example.demo.util.QrCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlMappingRepository repository;
    private final AtomicLong counter = new AtomicLong(100000);

    private static final String DOMAIN = "http://localhost:9090/";

    @Override
    public ShortenResponse createShortUrl(ShortenRequest request) {

        var existing = repository
                .findFirstByOriginalUrlAndIsActiveTrue(request.getLongUrl());

        if (existing.isPresent()) {
            UrlMapping mapping = existing.get();

            return ShortenResponse.builder()
                    .shortUrl(DOMAIN + mapping.getShortCode())
                    .originalUrl(mapping.getOriginalUrl())
                    .createdAt(mapping.getCreatedAt().toString())
                    .expiryTime(
                            mapping.getExpiryTime() != null
                                    ? mapping.getExpiryTime().toString()
                                    : null
                    )
                    .build();
        }

        String shortCode;

        if (request.getCustomAlias() != null &&
                !request.getCustomAlias().isBlank()) {

            if (repository.existsByShortCode(request.getCustomAlias())) {
                throw new RuntimeException("Alias already in use");
            }

            shortCode = request.getCustomAlias();
        } else {
            shortCode = generateUniqueCode();
        }

        LocalDateTime expiryTime = null;
        if (request.getExpiryMinutes() != null &&
                request.getExpiryMinutes() > 0) {

            expiryTime = LocalDateTime.now()
                    .plusMinutes(request.getExpiryMinutes());
        }

        UrlMapping mapping = UrlMapping.builder()
                .originalUrl(request.getLongUrl())
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now())
                .expiryTime(expiryTime)
                .clickCount(0L)
                .isActive(true)
                .build();

        repository.save(mapping);

        String shortUrl = DOMAIN + shortCode;

        String qrBase64 = QrCodeGenerator.generateBase64QRCode(shortUrl);

        return ShortenResponse.builder()
                .shortUrl(shortUrl)
                .originalUrl(mapping.getOriginalUrl())
                .createdAt(mapping.getCreatedAt().toString())
                .expiryTime(
                        mapping.getExpiryTime() != null
                                ? mapping.getExpiryTime().toString()
                                : null
                )
                .qrCodeBase64(qrBase64) 
                .build();
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = Base62Encoder.encode(counter.incrementAndGet());
        } while (repository.existsByShortCode(code));
        return code;
    }

    @Override
    public String getOriginalUrl(String shortCode) {

        UrlMapping mapping = repository
                .findByShortCodeAndIsActiveTrue(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        if (mapping.getExpiryTime() != null &&
                LocalDateTime.now().isAfter(mapping.getExpiryTime())) {

            throw new RuntimeException("Short URL has expired");
        }

        mapping.setClickCount(mapping.getClickCount() + 1);
        repository.save(mapping);

        return mapping.getOriginalUrl();
    }

    @Override
    public Long getClickCount(String shortCode) {
        return repository
                .findByShortCodeAndIsActiveTrue(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"))
                .getClickCount();
    }
    @Override
    public AnalyticsResponse getAnalytics(String shortCode) {

        UrlMapping mapping = repository
                .findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        return AnalyticsResponse.builder()
                .shortCode(mapping.getShortCode())
                .originalUrl(mapping.getOriginalUrl())
                .clickCount(mapping.getClickCount())
                .createdAt(mapping.getCreatedAt().toString())
                .expiryTime(
                        mapping.getExpiryTime() != null
                                ? mapping.getExpiryTime().toString()
                                : null
                )
                .isActive(mapping.getIsActive())
                .build();
        }
}