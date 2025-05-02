package com.cg.budgetboard.controller;

import com.cg.budgetboard.dto.CategoryRequest;
import com.cg.budgetboard.dto.CategoryResponse;
import com.cg.budgetboard.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<String> addCategory(@RequestBody CategoryRequest request) {
        categoryService.addCategory(request);
        return ResponseEntity.ok("Category added successfully.");
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategoriesByType(@RequestParam String type) {
        List<CategoryResponse> categories = categoryService.getCategoriesByType(type);
        return ResponseEntity.ok(categories);
    }
}
