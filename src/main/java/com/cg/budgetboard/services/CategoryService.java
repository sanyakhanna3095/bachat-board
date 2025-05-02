package com.cg.budgetboard.services;

import com.cg.budgetboard.dto.CategoryRequest;
import com.cg.budgetboard.dto.CategoryResponse;
import com.cg.budgetboard.model.Category;
import com.cg.budgetboard.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void addCategory(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .type(request.getType())
                .build();
        categoryRepository.save(category);
    }

    public List<CategoryResponse> getCategoriesByType(String type) {
        return categoryRepository.findByType(type)
                .stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .type(category.getType())
                        .build())
                .collect(Collectors.toList());
    }
}

