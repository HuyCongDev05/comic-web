package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.SQL.Categories;
import com.example.projectrestfulapi.dto.response.comic.CategoryResponseDTO;

public class CategoriesMapper {
    public static CategoryResponseDTO.ComicByCategory ComicByCategoryResponseDTO(Categories category) {
        CategoryResponseDTO.ComicByCategory categoryResponseDTO = new CategoryResponseDTO.ComicByCategory();
        categoryResponseDTO.setOriginName(category.getOriginName());
        categoryResponseDTO.setCategoriesName(category.getName());
        return categoryResponseDTO;
    }

    public static CategoryResponseDTO.DetailCategory DetailCategoryResponseDTO(Categories category) {
        CategoryResponseDTO.DetailCategory categoryResponseDTO = new CategoryResponseDTO.DetailCategory();
        categoryResponseDTO.setOriginName(category.getOriginName());
        categoryResponseDTO.setName(category.getName());
        categoryResponseDTO.setDetail(category.getDetail());
        return categoryResponseDTO;
    }
}
