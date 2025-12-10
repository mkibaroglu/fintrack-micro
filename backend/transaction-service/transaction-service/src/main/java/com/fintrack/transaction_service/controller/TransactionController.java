package com.fintrack.transaction_service.controller;

import com.fintrack.transaction_service.dto.*;
import com.fintrack.transaction_service.exception.BusinessException;
import com.fintrack.transaction_service.model.Category;
import com.fintrack.transaction_service.model.Transaction;
import com.fintrack.transaction_service.model.TransactionType;
import com.fintrack.transaction_service.service.TransactionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    public Transaction create(
            @RequestHeader(name = "X-User-Name", required = false) String username,
            @RequestBody CreateTransactionRequest request) {

        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing user header");
        }

        return service.createForUser(username, request);
    }

    @GetMapping("/{id}")
    public Transaction getOne(
            @RequestHeader(name = "X-User-Name", required = false) String username,
            @PathVariable Long id) {

        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing user header");
        }

        return service.getByIdForUser(id, username);
    }

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

    @GetMapping("/my")
    public Page<Transaction> getMyTransactions(
            @RequestHeader("X-User-Name") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) TransactionType type
    ) {
        return service.getMyTransactions(username, page, size, type);
    }

    @GetMapping("/summary")
    public MonthlySummaryResponse summary(
            @RequestHeader("X-User-Name") String username
    ) {
        return service.getMonthlySummary(username);
    }

    @GetMapping("/filter")
    public Page<Transaction> filterTransactions(
            @RequestHeader("X-User-Name") String username,

            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.filterTransactions(username, type,category, startDate, endDate, page, size);
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard(
            @RequestHeader("X-User-Name") String username
    ) {
        return service.getDashboard(username);
    }

    @GetMapping("/export/csv")
    public void exportCsv(
            @RequestHeader("X-User-Name") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size,
            HttpServletResponse response
    ) throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.csv");

        service.exportTransactionsToCsv(username, page, size, response.getWriter());
    }

    @GetMapping("/export/excel")
    public void exportExcel(
            @RequestHeader("X-User-Name") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size,
            HttpServletResponse response
    ) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.xlsx");

        service.exportTransactionsToExcel(username, page, size, response.getOutputStream());
    }

    @GetMapping("/dashboard/categories")
    public List<CategorySummary> categories(
            @RequestHeader("X-User-Name") String username
    ) {
        return service.getCategorySummary(username);
    }
}

