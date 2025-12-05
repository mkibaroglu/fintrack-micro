package com.fintrack.transaction_service.service;

import com.fintrack.transaction_service.model.Transaction;
import com.fintrack.transaction_service.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction create(Transaction tx) {
        return repository.save(tx);
    }

    public List<Transaction> getAll() {
        return repository.findAll();
    }

    public List<Transaction> getByUsername(String username) {
        return repository.findByUsername(username);
    }
}
