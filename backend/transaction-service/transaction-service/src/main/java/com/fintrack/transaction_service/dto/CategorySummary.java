package com.fintrack.transaction_service.dto;

import com.fintrack.transaction_service.model.Category;

import java.math.BigDecimal;

public class CategorySummary {
    private Category category;
    private BigDecimal total;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
