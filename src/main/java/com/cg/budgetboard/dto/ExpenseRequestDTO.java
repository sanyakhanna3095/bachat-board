package com.cg.budgetboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequestDTO {
    private Double amount;
    private String description;
    private LocalDate date;
    private Long categoryId;
}