package com.cg.budgetboard.controller;

import com.cg.budgetboard.dto.IncomeRequest;
import com.cg.budgetboard.dto.IncomeResponse;
import com.cg.budgetboard.services.IncomeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/incomes")
public class IncomeController {
    private IncomeService incomeService;

    // Add income
    @PostMapping
    public ResponseEntity<String> addIncome(@RequestBody IncomeRequest incomeRequest, HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = (auth!=null)? auth.substring(7):"";
        incomeService.addIncome(incomeRequest, token);  // No need to pass email
        return ResponseEntity.ok("Income added successfully.");
    }

    // Get all incomes
    @GetMapping
    public ResponseEntity<List<IncomeResponse>> getAllIncomes(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = (auth!=null)? auth.substring(7):"";
        List<IncomeResponse> incomes = incomeService.getAllIncomes(token);
        return ResponseEntity.ok(incomes);
    }

    // Update income
    @PutMapping("/{incomeId}")
    public ResponseEntity<String> updateIncome(@PathVariable Long incomeId, @RequestBody IncomeRequest incomeRequest, HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = (auth!=null)? auth.substring(7):"";
        incomeService.updateIncome(incomeId, incomeRequest, token);
        return ResponseEntity.ok("Income updated successfully.");
    }
}
