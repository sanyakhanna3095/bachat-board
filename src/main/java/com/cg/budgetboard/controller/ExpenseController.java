package com.cg.budgetboard.controller;

import com.cg.budgetboard.dto.ExpenseRequestDTO;
import com.cg.budgetboard.dto.ExpenseResponseDTO;
import com.cg.budgetboard.model.Expense;
import com.cg.budgetboard.services.ExpenseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<String> addExpense(@RequestBody ExpenseRequestDTO dto, HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = (auth!=null)? auth.substring(7):"";
        expenseService.addExpense(dto, token);
        return ResponseEntity.ok("Expense added successfully.");
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getAllExpenses(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(expenseService.getAllExpenses(token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(@PathVariable Long id,
                                                 @RequestBody ExpenseRequestDTO dto,
                                                 HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(expenseService.updateExpense(id, dto, token));
    }

}

