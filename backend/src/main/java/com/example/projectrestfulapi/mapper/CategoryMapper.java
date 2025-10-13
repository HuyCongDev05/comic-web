package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.SQL.Category;
import com.example.projectrestfulapi.dto.response.comic.CategoryResponseDTO;

public class CategoryMapper {
    public static CategoryResponseDTO.ComicByCategory ComicByCategoryResponseDTO(Category category) {
        CategoryResponseDTO.ComicByCategory categoryResponseDTO = new CategoryResponseDTO.ComicByCategory();
        categoryResponseDTO.setOriginName(category.getOriginName());
        categoryResponseDTO.setCategoryName(category.getName());
        return categoryResponseDTO;
    }

    public static CategoryResponseDTO.DetailCategory DetailCategoryResponseDTO(Category category) {
        CategoryResponseDTO.DetailCategory categoryResponseDTO = new CategoryResponseDTO.DetailCategory();
        categoryResponseDTO.setOriginName(category.getOriginName());
        categoryResponseDTO.setName(category.getName());
        categoryResponseDTO.setDetail(category.getDetail());
        return categoryResponseDTO;
    }
}
