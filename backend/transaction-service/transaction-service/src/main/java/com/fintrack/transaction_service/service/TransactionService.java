package com.fintrack.transaction_service.service;

import com.fintrack.transaction_service.dto.CreateTransactionRequest;
import com.fintrack.transaction_service.dto.UpdateTransactionRequest;
import com.fintrack.transaction_service.exception.BusinessException;
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

    public Transaction createForUser(String username, CreateTransactionRequest req) {
        Transaction tx = new Transaction();
        tx.setUsername(username);
        tx.setType(req.getType());
        tx.setDescription(req.getDescription());
        tx.setAmount(req.getAmount());
        tx.setCreatedAt(java.time.LocalDateTime.now());

        return repository.save(tx);
    }

    public List<Transaction> getAllByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Transaction getByIdForUser(Long id, String username) {
        return repository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new BusinessException("Transaction bulunamadı veya size ait değil."));
    }

    public void deleteForUser(Long id, String username) {
        Transaction tx = repository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new BusinessException("Transaction bulunamadı veya size ait değil."));
        repository.delete(tx);
    }

    public Transaction updateForUser(Long id, String username, UpdateTransactionRequest req) {
        Transaction tx = repository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new BusinessException("Transaction bulunamadı veya size ait değil."));

        tx.setType(req.getType());
        tx.setDescription(req.getDescription());
        tx.setAmount(req.getAmount());
        return repository.save(tx);
    }
}
