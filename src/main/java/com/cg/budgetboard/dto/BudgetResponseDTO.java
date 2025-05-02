package com.cg.budgetboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetResponseDTO {
    private Long id;
    private Double amount;
    private String month;
    private Long categoryId;
    private String categoryName;
    private String categoryType;
}