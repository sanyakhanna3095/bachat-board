package com.cg.budgetboard.services;

import com.cg.budgetboard.model.Expense;
import com.cg.budgetboard.model.Income;
import com.cg.budgetboard.model.User;
import com.cg.budgetboard.repository.ExpenseRepository;
import com.cg.budgetboard.repository.IncomeRepository;
import com.cg.budgetboard.util.AuthUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AuthUtil authUtil;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final EmailService emailService;

    public void sendMonthlySummaryReport(String month, String token) {
        User user = authUtil.getCurrentUser(token);

        List<Income> incomes = incomeRepository.findByUserAndMonth(user.getId(), month);
        incomes.sort(Comparator.comparing(Income::getDate));
        List<Expense> expenses = expenseRepository.findByUserIdAndMonth(user.getId(), month);
        expenses.sort(Comparator.comparing(Expense::getDate));

        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();

        ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, pdf);
            doc.open();

            doc.add(new Paragraph("Monthly Summary Report for " + month));
            doc.add(new Paragraph("\nINCOME"));
            addTable(
                    doc,
                    incomes.stream()
                            .map(i -> List.of(i.getDate().toString(),i.getCategory().getName(), i.getAmount().toString()))
                            .toList(),
                    List.of("Date","Category", "Amount")
            );

            doc.add(new Paragraph("\nEXPENSE"));
            addTable(doc, expenses.stream().map(e -> List.of(e.getDate().toString(),e.getDescription(), e.getCategory().getName(), e.getAmount().toString())).toList(), List.of("Date","Description", "Category", "Amount"));

            doc.add(new Paragraph("\nComparison:"));
            doc.add(new Paragraph("Total Income: " + totalIncome));
            doc.add(new Paragraph("Total Expense: " + totalExpense));
            doc.add(new Paragraph("Balance: " + (totalIncome - totalExpense)));

            doc.close();

            emailService.sendReportEmail(user.getEmail(), "Monthly Summary Report - " + month, pdf.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    public void sendSpendingTrendsReport(String month, String token) {
        User user = authUtil.getCurrentUser(token);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth ym = YearMonth.parse(month, formatter);
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();

        List<Expense> expenses = expenseRepository.findByUserIdAndDateRange(user.getId(), startDate, endDate);

        Map<String, Double> categoryTotals = new HashMap<>();
        double totalExpense = 0;

        for (Expense e : expenses) {
            String category = e.getCategory().getName();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + e.getAmount());
            totalExpense += e.getAmount();
        }

        ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, pdf);
            doc.open();

            doc.add(new Paragraph("Spending Trends Report (" + month + ")"));
            List<List<String>> rows = new ArrayList<>();

            for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                String category = entry.getKey();
                Double amt = entry.getValue();
                double percentage = (amt / totalExpense) * 100;
                rows.add(List.of(category, String.format("%.2f", amt), String.format("%.2f%%", percentage)));
            }

            addTable(doc, rows, List.of("Category", "Amount", "Percentage"));
            doc.add(new Paragraph("Total Spent: " + totalExpense));
            doc.close();

            emailService.sendReportEmail(user.getEmail(), "Spending Trends Report - " + month, pdf.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate trends PDF", e);
        }
    }

    private void addTable(Document doc, List<List<String>> data, List<String> headers) throws DocumentException {
        PdfPTable table = new PdfPTable(headers.size());
        headers.forEach(h -> table.addCell(new PdfPCell(new Phrase(h))));

        for (List<String> row : data) {
            row.forEach(cell -> table.addCell(new PdfPCell(new Phrase(cell))));
        }
        doc.add(table);
    }
}
