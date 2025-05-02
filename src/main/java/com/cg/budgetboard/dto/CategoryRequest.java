package com.cg.budgetboard.dto;


import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String type; // EXPENSE or INCOME
}