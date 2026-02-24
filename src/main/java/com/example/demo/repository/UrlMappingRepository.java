package com.example.demo.repository;
import com.example.demo.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findFirstByOriginalUrlAndIsActiveTrue(String originalUrl);
    Optional<UrlMapping> findByShortCodeAndIsActiveTrue(String shortCode);
    boolean existsByShortCode(String shortCode);
    Optional<UrlMapping> findByShortCode(String shortCode);
}