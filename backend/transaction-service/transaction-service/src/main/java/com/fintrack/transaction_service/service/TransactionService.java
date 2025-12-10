package com.fintrack.transaction_service.service;

import com.fintrack.transaction_service.dto.*;
import com.fintrack.transaction_service.exception.BusinessException;
import com.fintrack.transaction_service.model.Category;
import com.fintrack.transaction_service.model.Transaction;
import com.fintrack.transaction_service.model.TransactionType;
import com.fintrack.transaction_service.repository.TransactionRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        tx.setCategory(req.getCategory());
        tx.setDescription(req.getDescription());
        tx.setAmount(req.getAmount());
        tx.setCreatedAt(java.time.LocalDateTime.now());

        return repository.save(tx);
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
        tx.setCategory(req.getCategory());
        tx.setDescription(req.getDescription());
        tx.setAmount(req.getAmount());
        return repository.save(tx);
    }

    public Page<Transaction> getMyTransactions(String username, int page, int size, TransactionType type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        if (type == null) {
            return repository.findByUsername(username, pageable);
        }

        return repository.findByUsernameAndType(username, type, pageable);
    }

    public MonthlySummaryResponse getMonthlySummary(String username) {
        BigDecimal income = repository.totalIncome(username);
        BigDecimal expense = repository.totalExpense(username);
        BigDecimal balance = income.subtract(expense);

        return new MonthlySummaryResponse(income, expense, balance);
    }

    public Page<Transaction> filterTransactions(
            String username,
            TransactionType type,
            Category category,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.atTime(23, 59, 59) : null;

        if (type != null && category != null && start != null && end != null) {
            return repository.findByUsernameAndTypeAndCategoryAndCreatedAtBetween(
                    username, type, category, start, end, pageable
            );
        }

        if (type != null && category != null) {
            return repository.findByUsernameAndTypeAndCategory(
                    username, type, category, pageable
            );
        }

        if (category != null) {
            return repository.findByUsernameAndCategory(username, category, pageable);
        }

        if (type != null && start != null && end != null) {
            return repository.findByUsernameAndTypeAndCreatedAtBetween(username, type, start, end, pageable);
        }

        if (type != null) {
            return repository.findByUsernameAndType(username, type, pageable);
        }

        if (start != null && end != null) {
            return repository.findByUsernameAndCreatedAtBetween(username, start, end, pageable);
        }

        return repository.findByUsername(username, pageable);
    }

    public DashboardResponse getDashboard(String username) {

        BigDecimal income = repository.totalIncome(username);
        BigDecimal expense = repository.totalExpense(username);
        BigDecimal balance = income.subtract(expense);
        int count = repository.countAll(username);

        return new DashboardResponse(income, expense, balance, count);
    }

    public void exportTransactionsToCsv(String username, int page, int size, Writer writer) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = repository.findByUsername(username, pageable);

        PrintWriter pw = new PrintWriter(writer);
        pw.println("ID,TYPE,CATEGORY,AMOUNT,DESCRIPTION,CREATED_AT");

        for (Transaction tx : transactions.getContent()) {
            pw.println(
                    tx.getId() + "," +
                            tx.getType() + "," +
                            tx.getCategory() + "," +
                            tx.getAmount() + "," +
                            tx.getDescription() + "," +
                            tx.getCreatedAt()
            );
        }

        pw.flush();
    }

    public void exportTransactionsToExcel(
            String username,
            int page,
            int size,
            OutputStream outputStream
    ) throws IOException {

        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = repository.findByUsername(username, pageable);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("TYPE");
        header.createCell(2).setCellValue("CATEGORY");
        header.createCell(3).setCellValue("AMOUNT");
        header.createCell(4).setCellValue("DESCRIPTION");
        header.createCell(5).setCellValue("CREATED_AT");

        int rowIdx = 1;

        for (Transaction tx : transactions.getContent()) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(tx.getId());
            row.createCell(1).setCellValue(tx.getType().name());
            row.createCell(2).setCellValue(tx.getCategory().name());
            row.createCell(3).setCellValue(tx.getAmount().doubleValue());
            row.createCell(4).setCellValue(tx.getDescription());
            row.createCell(5).setCellValue(tx.getCreatedAt().toString());
        }

        workbook.write(outputStream);
        workbook.close();
    }

    public List<CategorySummary> getCategorySummary(String username) {
        List<Object[]> rows = repository.expenseGroupByCategory(username);

        return rows.stream().map(r -> {
            CategorySummary dto = new CategorySummary();
            dto.setCategory((Category) r[0]);
            dto.setTotal((BigDecimal) r[1]);
            return dto;
        }).toList();
    }
}
