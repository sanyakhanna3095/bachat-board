package com.cg.budgetboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public @Data class IncomeResponse {
    private Long id;
    private Double amount;
    private LocalDate date;
    private String categoryName;
}
