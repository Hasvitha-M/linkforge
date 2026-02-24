package com.example.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "URL Shortener API",
        version = "1.0",
        description = "Scalable URL shortening service built with Spring Boot"
    )
)

@SpringBootApplication

public class UrlApplication{
	public static void main(String[] args) {
		SpringApplication.run(UrlApplication.class, args);
	}
}