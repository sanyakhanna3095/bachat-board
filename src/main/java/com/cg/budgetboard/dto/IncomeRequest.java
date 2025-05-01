package com.cg.budgetboard.dto;

import lombok.Data;

import java.time.LocalDate;

public @Data class IncomeRequest {
    private Double amount;
    private LocalDate date;
    private Long categoryId; // sending category by ID
}
