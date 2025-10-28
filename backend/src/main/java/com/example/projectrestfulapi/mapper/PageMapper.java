package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;

import java.util.List;

public class PageMapper {
    public static ComicResponseDTO.PageResponseDTO mapComicResponseDTOPage(List<ComicResponseDTO.ComicInfoResponseDTO> comicInfoResponseDTOList, Long totalPages) {
        if (comicInfoResponseDTOList == null || totalPages == null) return null;
        ComicResponseDTO.PageResponseDTO pageResponseDTO = new ComicResponseDTO.PageResponseDTO();
        pageResponseDTO.setContent(comicInfoResponseDTOList);
        pageResponseDTO.setTotalPages(totalPages);
        return pageResponseDTO;
    }
}
