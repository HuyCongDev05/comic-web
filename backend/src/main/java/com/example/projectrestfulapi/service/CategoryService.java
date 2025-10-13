package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Category;
import com.example.projectrestfulapi.repository.SQL.CategoryRepository;
import com.example.projectrestfulapi.repository.SQL.ComicCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ComicCategoryRepository comicCategoryRepository;

    public CategoryService(CategoryRepository categoryRepository, ComicCategoryRepository comicCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.comicCategoryRepository = comicCategoryRepository;
    }

    public List<Category> handleFindAll() {
        return categoryRepository.findAll();
    }

    public List<Category> handleGetCategoryByComicId(Long comicId) {
        return categoryRepository.getCategoryByComicId(comicId);
    }
}
