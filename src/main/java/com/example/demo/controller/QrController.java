package com.example.demo.controller;

import com.example.demo.util.QrCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QrController {

    private static final String DOMAIN = "http://localhost:9090/";

    @GetMapping(value = "/qr/{code}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getQr(@PathVariable String code) {

        String url = DOMAIN + code;

        String base64 = QrCodeGenerator.generateBase64QRCode(url);

        return Base64.getDecoder().decode(base64);
    }
}