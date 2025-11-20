package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Categories;
import com.example.projectrestfulapi.dto.response.Dashboard.DashboardResponseDTO;
import com.example.projectrestfulapi.repository.SQL.CategoriesRepository;
import com.example.projectrestfulapi.repository.SQL.ComicCategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {
    private final CategoriesRepository categoryRepository;
    private final ComicCategoriesRepository comicCategoriesRepository;

    public CategoriesService(CategoriesRepository categoryRepository, ComicCategoriesRepository comicCategoriesRepository) {
        this.categoryRepository = categoryRepository;
        this.comicCategoriesRepository = comicCategoriesRepository;
    }

    public List<Categories> handleFindAll() {
        return categoryRepository.findAll();
    }

    public List<Categories> handleGetCategoryByComicId(Long comicId) {
        return categoryRepository.getCategoriesByComicId(comicId);
    }

    public List<DashboardResponseDTO.HomeDashboard.CategoriesRatio> handleGetCategoryRatio() {
        return comicCategoriesRepository.getCategoriesRatio();
    }
}
