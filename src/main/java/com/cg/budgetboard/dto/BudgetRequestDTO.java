package com.cg.budgetboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetRequestDTO {
    private Double amount;
    private String month;
    private Long categoryId;
}
