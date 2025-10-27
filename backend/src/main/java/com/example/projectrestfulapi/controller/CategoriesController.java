package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Categories;
import com.example.projectrestfulapi.dto.response.comic.CategoryResponseDTO;
import com.example.projectrestfulapi.mapper.CategoriesMapper;
import com.example.projectrestfulapi.service.CategoriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1")
public class CategoriesController {
    private final CategoriesService categoryService;

    public CategoriesController(CategoriesService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDTO.DetailCategory>> getAllCategories() {
        List<Categories> categoryList = categoryService.handleFindAll();
        List<CategoryResponseDTO.DetailCategory> categoryResponseDTOList = categoryList.stream()
                .map(CategoriesMapper::DetailCategoryResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(categoryResponseDTOList);
    }
}
