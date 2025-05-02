package com.cg.budgetboard.services;

import com.cg.budgetboard.dto.IncomeRequest;
import com.cg.budgetboard.dto.IncomeResponse;
import com.cg.budgetboard.dto.ResponseDTO;
import com.cg.budgetboard.model.Income;

import java.util.List;

public interface IncomeInterface {
    Income addIncome(IncomeRequest incomeRequest, String token);

    List<IncomeResponse> getAllIncomes(String token);
}
