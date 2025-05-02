package com.cg.budgetboard.services;

import com.cg.budgetboard.dto.IncomeRequest;
import com.cg.budgetboard.dto.IncomeResponse;
import com.cg.budgetboard.dto.ResponseDTO;
import com.cg.budgetboard.exceptionhandler.CustomException;
import com.cg.budgetboard.model.Category;
import com.cg.budgetboard.model.Income;
import com.cg.budgetboard.model.User;
import com.cg.budgetboard.repository.CategoryRepository;
import com.cg.budgetboard.repository.IncomeRepository;
import com.cg.budgetboard.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IncomeService implements IncomeInterface{
    @Autowired
    IncomeRepository incomeRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Income addIncome(IncomeRequest incomeRequest, String token){
        User user=authUtil.getCurrentUser(token);
        Category category=categoryRepository.findById(incomeRequest.getCategoryId())
                .orElseThrow(()-> new CustomException("Category not found"));
        if (!"INCOME".equalsIgnoreCase(category.getType())) {
            throw new CustomException("Category is not of type INCOME");
        }

//        now if category type is income, we will create a new income object and save it
        Income income = new Income();
        income.setAmount(incomeRequest.getAmount());
        income.setDate(incomeRequest.getDate());
        income.setCategory(category);
//        makes it a user specific data
        income.setUser(user);
        log.info("Income added successfully!");
        return incomeRepository.save(income);

    }

    @Override
    public List<IncomeResponse> getAllIncomes(String token){
        User user=authUtil.getCurrentUser(token);

        List<Income> incomes=incomeRepository.findByUser(user);
        incomes.sort(Comparator.comparing(Income::getDate));
        List<IncomeResponse> incomeResponses = incomes.stream().map(income -> {
            IncomeResponse incomeResponse = new IncomeResponse();
            incomeResponse.setId(income.getId());
            incomeResponse.setAmount(income.getAmount());
            incomeResponse.setDate(income.getDate());
            incomeResponse.setCategoryName(income.getCategory().getName());
            return incomeResponse;
        }).collect(Collectors.toList());
        return incomeResponses;
    }


    public Income updateIncome(Long incomeId, IncomeRequest incomeRequest, String token){
        User user = authUtil.getCurrentUser(token);

        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new CustomException("Income not found"));

        if (!income.getUser().getId().equals(user.getId())) {
            throw new CustomException("Unauthorized to update this income.");
        }

        Category category = categoryRepository.findById(incomeRequest.getCategoryId())
                .orElseThrow(() -> new CustomException("Category not found"));

        if (!"INCOME".equalsIgnoreCase(category.getType())) {
            throw new CustomException("Category is not of type INCOME");
        }

        income.setAmount(incomeRequest.getAmount());
        income.setDate(incomeRequest.getDate());
        income.setCategory(category);

        return incomeRepository.save(income);
    }

}
