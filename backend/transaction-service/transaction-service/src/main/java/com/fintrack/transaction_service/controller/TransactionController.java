package com.fintrack.transaction_service.controller;

import com.fintrack.transaction_service.exception.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Transaction Service";
    }

    @GetMapping("/error")
    public String error() {
        // Test amaçlı bilerek hata atıyoruz
        throw new BusinessException("Örnek business hatası: Bakiye yetersiz.");
    }
}
