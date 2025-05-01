package com.cg.budgetboard.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeRequest {
    private Double amount;
    private LocalDate date;
    private Long categoryId; // sending category by ID
}

