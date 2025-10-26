package com.example.projectrestfulapi.dto.response.page;

import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;

import java.util.List;

public class PageResponseDTO {
    private List<ComicResponseDTO.ComicInfoResponseDTO> comics;
    private int totalItems;
    private int currentPage;
    private int totalPage;
}
