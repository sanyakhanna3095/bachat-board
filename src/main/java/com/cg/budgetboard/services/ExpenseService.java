package com.cg.budgetboard.services;

import com.cg.budgetboard.dto.ExpenseRequestDTO;
import com.cg.budgetboard.dto.ExpenseResponseDTO;
import com.cg.budgetboard.exceptionhandler.CustomException;
import com.cg.budgetboard.model.Budget;
import com.cg.budgetboard.model.Category;
import com.cg.budgetboard.model.Expense;
import com.cg.budgetboard.model.User;
import com.cg.budgetboard.repository.BudgetRepository;
import com.cg.budgetboard.repository.CategoryRepository;
import com.cg.budgetboard.repository.ExpenseRepository;
import com.cg.budgetboard.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final AuthUtil authUtil;
    private final EmailService emailService;

    public Expense addExpense(ExpenseRequestDTO dto, String token) {
        User user = authUtil.getCurrentUser(token);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CustomException("Category not found"));

        if (!"EXPENSE".equalsIgnoreCase(category.getType())) {
            throw new CustomException("Category is not of type EXPENSE");
        }

        // Check if budget exists for the user and category
        String expenseMonth = dto.getDate().format(DateTimeFormatter.ofPattern("MM-yyyy"));

        Budget budget = budgetRepository.findByUserIdAndCategoryIdAndMonth(user.getId(), category.getId(), expenseMonth)
                .orElseThrow(() -> new RuntimeException("No budget set for this category in " + expenseMonth));
        // Calculate total expenses for the category
        Double totalExpenses = expenseRepository.getTotalExpenseByUserAndCategory(user.getId(), category.getId());

        double newTotal = totalExpenses + dto.getAmount();
        double budgetLimit = budget.getAmount();

        if (newTotal > 0.7 * budgetLimit) {
            emailService.sendEmail(user.getEmail(), "Expense Alert",
                    "You've exceeded 70% of your allotted budget for category: " + category.getName());
        }

        Expense expense = Expense.builder()
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .date(dto.getDate())
                .user(user)
                .category(category)
                .build();

        return expenseRepository.save(expense);
    }

    public List<ExpenseResponseDTO> getAllExpenses(String token) {
        User user = authUtil.getCurrentUser(token);
        List<Expense> expenses = expenseRepository.findByUserId(user.getId());

        return expenses.stream().map(exp -> new ExpenseResponseDTO(
                exp.getId(),
                exp.getAmount(),
                exp.getDescription(),
                exp.getDate(),
                exp.getCategory().getName()
        )).collect(Collectors.toList());
    }

    public ExpenseResponseDTO updateExpense(Long id, ExpenseRequestDTO dto, String token) {
        User user = authUtil.getCurrentUser(token);
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new CustomException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new CustomException("Unauthorized");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CustomException("Category not found"));

        if (!"EXPENSE".equalsIgnoreCase(category.getType())) {
            throw new CustomException("Category is not of type EXPENSE");
        }

        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setDescription(dto.getDescription());
        expense.setCategory(category);

        Expense updatedExpense = expenseRepository.save(expense);

        return new ExpenseResponseDTO(
                updatedExpense.getId(),
                updatedExpense.getAmount(),
                updatedExpense.getDescription(),
                updatedExpense.getDate(),
                updatedExpense.getCategory().getName()
        );
    }
}


