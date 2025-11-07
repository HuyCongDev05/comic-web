package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;

import java.util.List;

public class PageMapper {
    public static ComicResponseDTO.PageResponseDTO mapComicResponseDTOPage(List<ComicResponseDTO.ComicInfoResponseDTO> comicInfoResponseDTOList, int totalPages, int currentPageSize) {
        if (comicInfoResponseDTOList == null) return null;
        ComicResponseDTO.PageResponseDTO pageResponseDTO = new ComicResponseDTO.PageResponseDTO();
        pageResponseDTO.setContent(comicInfoResponseDTOList);
        pageResponseDTO.setCurrentPageSize(currentPageSize);
        pageResponseDTO.setTotalPages(totalPages);
        return pageResponseDTO;
    }
}
