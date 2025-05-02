package com.cg.budgetboard.controller;

import com.cg.budgetboard.dto.BudgetRequestDTO;
import com.cg.budgetboard.dto.BudgetResponseDTO;
import com.cg.budgetboard.services.BudgetService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponseDTO> addBudget(@RequestBody BudgetRequestDTO dto, HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = (auth!=null)? auth.substring(7):"";
        return ResponseEntity.ok(budgetService.addBudget(dto, token));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponseDTO>> getAllBudgets(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = (auth!=null)? auth.substring(7):"";
        return ResponseEntity.ok(budgetService.getAllBudgets(token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> updateBudget(@PathVariable Long id,
                                                       @RequestBody BudgetRequestDTO dto,
                                                       HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = (auth!=null)? auth.substring(7):"";
        return ResponseEntity.ok(budgetService.updateBudget(id, dto, token));
    }
}
