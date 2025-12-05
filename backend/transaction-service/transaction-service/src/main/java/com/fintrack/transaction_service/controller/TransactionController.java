package com.fintrack.transaction_service.controller;

import com.fintrack.transaction_service.dto.CreateTransactionRequest;
import com.fintrack.transaction_service.dto.UpdateTransactionRequest;
import com.fintrack.transaction_service.exception.BusinessException;
import com.fintrack.transaction_service.model.Transaction;
import com.fintrack.transaction_service.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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

    @GetMapping
    public List<Transaction> getByLoggedInUser(
            @RequestHeader(name = "X-User-Name", required = false) String username
    ) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing user header");
        }

        System.out.println("GET /api/transactions - username: " + username);
        return service.getAllByUsername(username);
    }

    @PostMapping
    public Transaction create(
            @RequestHeader(name = "X-User-Name", required = false) String username,
            @RequestBody CreateTransactionRequest request) {

        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing user header");
        }

        return service.createForUser(username, request);
    }

    // --- KULLANICININ TÜM İŞLEMLERİ (header'daki user'a göre) ---
    @GetMapping("/me")
    public List<Transaction> getMyTransactions(
            @RequestHeader(name = "X-User-Name", required = false) String username) {

        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing user header");
        }

        return service.getAllByUsername(username);
    }

    // --- TEK TRANSACTION DETAYI (sadece owner erişebilir) ---
    @GetMapping("/{id}")
    public Transaction getOne(
            @RequestHeader(name = "X-User-Name", required = false) String username,
            @PathVariable Long id) {

        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing user header");
        }

        return service.getByIdForUser(id, username);
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public Transaction update(
            @RequestHeader(name = "X-User-Name", required = false) String username,
            @PathVariable Long id,
            @RequestBody UpdateTransactionRequest request) {

        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing user header");
        }

        return service.updateForUser(id, username, request);
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestHeader(name = "X-User-Name", required = false) String username,
            @PathVariable Long id) {

        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing user header");
        }

        service.deleteForUser(id, username);
    }
}

