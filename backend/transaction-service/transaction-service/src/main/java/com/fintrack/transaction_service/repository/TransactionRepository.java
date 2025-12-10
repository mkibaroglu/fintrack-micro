package com.fintrack.transaction_service.repository;


import com.fintrack.transaction_service.model.Category;
import com.fintrack.transaction_service.model.Transaction;
import com.fintrack.transaction_service.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    Optional<Transaction> findByIdAndUsername(Long id, String username);

    void deleteByIdAndUsername(Long id, String username);

    Page<Transaction> findByUsername(String username, Pageable pageable);

    Page<Transaction> findByUsernameAndType(String username, TransactionType type, Pageable pageable);

    Page<Transaction> findByUsernameAndCreatedAtBetween(String username, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Transaction> findByUsernameAndTypeAndCreatedAtBetween(String username, TransactionType type, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT COALESCE(SUM(t.amount),0) FROM Transaction t WHERE t.username = :username AND t.type = 'INCOME'")
    BigDecimal totalIncome(String username);

    @Query("SELECT COALESCE(SUM(t.amount),0) FROM Transaction t WHERE t.username = :username AND t.type = 'EXPENSE'")
    BigDecimal totalExpense(String username);

    Page<Transaction> findByUsernameAndCategory(
            String username,
            Category category,
            Pageable pageable
    );

    Page<Transaction> findByUsernameAndTypeAndCategory(
            String username,
            TransactionType type,
            Category category,
            Pageable pageable
    );

    Page<Transaction> findByUsernameAndTypeAndCategoryAndCreatedAtBetween(
            String username,
            TransactionType type,
            Category category,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.username = :username")
    int countAll(String username);

    @Query("""
SELECT t.category, SUM(t.amount)
FROM Transaction t
WHERE t.username = :username AND t.type = 'EXPENSE'
GROUP BY t.category
""")
    List<Object[]> expenseGroupByCategory(String username);
}
