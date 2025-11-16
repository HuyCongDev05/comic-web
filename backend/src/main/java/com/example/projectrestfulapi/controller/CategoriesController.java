package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Categories;
import com.example.projectrestfulapi.dto.response.comic.CategoryResponseDTO;
import com.example.projectrestfulapi.mapper.CategoriesMapper;
import com.example.projectrestfulapi.service.CategoriesService;
import com.example.projectrestfulapi.service.TotalVisitService;
import com.example.projectrestfulapi.util.Security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1")
public class CategoriesController {
    private final CategoriesService categoryService;
    private final TotalVisitService totalVisitService;
    private final JwtUtil jwtUtil;

    public CategoriesController(CategoriesService categoryService, TotalVisitService totalVisitService, JwtUtil jwtUtil) {
        this.categoryService = categoryService;
        this.totalVisitService = totalVisitService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDTO.DetailCategory>> getAllCategories(HttpServletRequest request, HttpServletResponse response) {
        String visitTokenRequest = Optional.ofNullable(request.getCookies()).stream().flatMap(Arrays::stream)
                .filter(c -> "visit_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (visitTokenRequest == null) {
            totalVisitService.handleSaveTotalVisit();
            String visitToken = jwtUtil.createVisitToken();
            ResponseCookie cookie = ResponseCookie.from("visit_token", visitToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(60*60)
                    .sameSite("None")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        List<Categories> categoryList = categoryService.handleFindAll();
        List<CategoryResponseDTO.DetailCategory> categoryResponseDTOList = categoryList.stream()
                .map(CategoriesMapper::DetailCategoryResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(categoryResponseDTOList);
    }
}
