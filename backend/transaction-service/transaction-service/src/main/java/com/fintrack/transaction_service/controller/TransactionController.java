package com.fintrack.transaction_service.controller;

import com.fintrack.transaction_service.exception.BusinessException;
import com.fintrack.transaction_service.model.Transaction;
import com.fintrack.transaction_service.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Transaction Service";
    }

    @GetMapping("/error")
    public String error() {
        throw new BusinessException("Örnek business hatası: Bakiye yetersiz.");
    }

    @PostMapping
    public Transaction create(@RequestBody Transaction tx) {
        return service.create(tx);
    }

    @GetMapping
    public List<Transaction> getAll() {
        return service.getAll();
    }

    @GetMapping("/user/{username}")
    public List<Transaction> getByUser(@PathVariable String username) {
        return service.getByUsername(username);
    }

    @GetMapping("/api/transactions")
    public List<Transaction> getAllTransactions(@RequestHeader(name = "X-User-Name", required = false) String username) {
        System.out.println("Gateway'den gelen username: " + username);
        return service.getAll();
    }
}
