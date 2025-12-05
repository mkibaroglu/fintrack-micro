package com.fintrack.transaction_service.repository;


import com.fintrack.transaction_service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUsername(String username);

    Optional<Transaction> findByIdAndUsername(Long id, String username);

    void deleteByIdAndUsername(Long id, String username);
}
