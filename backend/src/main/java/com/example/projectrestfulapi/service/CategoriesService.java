package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Categories;
import com.example.projectrestfulapi.repository.SQL.CategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {
    private final CategoriesRepository categoryRepository;

    public CategoriesService(CategoriesRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Categories> handleFindAll() {
        return categoryRepository.findAll();
    }

    public List<Categories> handleGetCategoryByComicId(Long comicId) {
        return categoryRepository.getCategoriesByComicId(comicId);
    }
}
