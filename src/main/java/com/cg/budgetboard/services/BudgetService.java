package com.cg.budgetboard.services;

import com.cg.budgetboard.dto.BudgetRequestDTO;
import com.cg.budgetboard.dto.BudgetResponseDTO;
import com.cg.budgetboard.exceptionhandler.CustomException;
import com.cg.budgetboard.model.Budget;
import com.cg.budgetboard.model.Category;
import com.cg.budgetboard.model.User;
import com.cg.budgetboard.repository.BudgetRepository;
import com.cg.budgetboard.repository.CategoryRepository;
import com.cg.budgetboard.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final AuthUtil authUtil;

    public BudgetResponseDTO addBudget(BudgetRequestDTO budgetDTO, String token) {
        User user = authUtil.getCurrentUser(token);
        Category category = categoryRepository.findById(budgetDTO.getCategoryId())
                .orElseThrow(() -> new CustomException("Category not found"));

        if (!"EXPENSE".equalsIgnoreCase(category.getType())) {
            throw new CustomException("Category is not of type EXPENSE");
        }

        Budget budget = Budget.builder()
                .amount(budgetDTO.getAmount())
                .month(budgetDTO.getMonth())
                .user(user)
                .category(category)
                .build();

        Budget savedBudget = budgetRepository.save(budget);
        return BudgetMapper.toDto(savedBudget);
    }

    public List<BudgetResponseDTO> getAllBudgets(String token) {
        User user = authUtil.getCurrentUser(token);
        return budgetRepository.findByUserId(user.getId())
                .stream()
                .map(BudgetMapper::toDto)
                .toList();
    }

    public BudgetResponseDTO updateBudget(Long budgetId, BudgetRequestDTO dto, String token) {
        User user = authUtil.getCurrentUser(token);
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new CustomException("Budget not found"));

        if (!budget.getUser().getId().equals(user.getId())) {
            throw new CustomException("Unauthorized");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CustomException("Category not found"));

        if (!"EXPENSE".equalsIgnoreCase(category.getType())) {
            throw new CustomException("Category is not of type EXPENSE");
        }

        budget.setAmount(dto.getAmount());
        budget.setMonth(dto.getMonth());
        budget.setCategory(category);

        Budget updated = budgetRepository.save(budget);
        return BudgetMapper.toDto(updated);
    }

    public void deleteBudget(Long budgetId, String token) {
        User user = authUtil.getCurrentUser(token);
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new CustomException("Budget not found"));

        if (!budget.getUser().getId().equals(user.getId())) {
            throw new CustomException("Unauthorized");
        }

        budgetRepository.delete(budget);
    }
}
class BudgetMapper {
    public static BudgetResponseDTO toDto(Budget budget) {
        return BudgetResponseDTO.builder()
                .id(budget.getId())
                .amount(budget.getAmount())
                .month(budget.getMonth())
                .categoryId(budget.getCategory().getId())
                .categoryName(budget.getCategory().getName())
                .categoryType(budget.getCategory().getType())
                .build();
    }
}