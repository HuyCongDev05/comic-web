package com.example.projectrestfulapi.util;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.domain.SQL.ComicStats;
import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;
import com.example.projectrestfulapi.mapper.ComicMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ComicUtil {
    private ComicUtil() {
    }

    public static List<ComicResponseDTO.ComicInfoResponseDTO> mapComicsWithRatings(
            List<Comic> comics, List<ComicStats> comicStats) {

        if (comics == null || comicStats == null) {
            throw new IllegalArgumentException("Comic or ComicStats list must not be null");
        }

        List<ComicResponseDTO.ComicInfoResponseDTO> result = new ArrayList<>();

        for (Comic comic : comics) {
            BigDecimal rating = BigDecimal.ZERO;

            for (ComicStats stat : comicStats) {
                if (stat.getComic().getId().equals(comic.getId())) {
                    rating = stat.getAvgRating();
                    break;
                }
            }

            result.add(ComicMapper.mapComicInfoResponseDTO(comic, rating));
        }
        return result;
    }
}
