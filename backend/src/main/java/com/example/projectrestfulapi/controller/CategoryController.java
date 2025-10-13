package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Category;
import com.example.projectrestfulapi.dto.response.comic.CategoryResponseDTO;
import com.example.projectrestfulapi.mapper.CategoryMapper;
import com.example.projectrestfulapi.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDTO.DetailCategory>> getAllCategories() {
        List<Category> categoryList = categoryService.handleFindAll();
        List<CategoryResponseDTO.DetailCategory> categoryResponseDTOList = categoryList.stream()
                .map(CategoryMapper::DetailCategoryResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(categoryResponseDTOList);
    }
}
