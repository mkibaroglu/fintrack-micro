package com.fintrack.transaction_service.dto;

import com.fintrack.transaction_service.model.Category;
import com.fintrack.transaction_service.model.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;

public class CreateTransactionRequest {
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private String description;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private Category category;

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
