package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.SQL.Category;
import com.example.projectrestfulapi.dto.response.comic.CategoryResponseDTO;

public class CategoryMapper {
    public static CategoryResponseDTO.categoryByCategory categoryToCategoryResponseDTO(Category category){
        CategoryResponseDTO.categoryByCategory categoryResponseDTO = new CategoryResponseDTO.categoryByCategory();
        categoryResponseDTO.setOriginName(category.getOriginName());
        categoryResponseDTO.setName(category.getName());
        categoryResponseDTO.setDetail(category.getDetail());
        return categoryResponseDTO;
    }
}
