package com.cg.budgetboard.dto;

import lombok.Data;

import java.time.LocalDate;

public @Data class IncomeResponse {
    private Long id;
    private Double amount;
    private LocalDate date;
    private String categoryName;
}
