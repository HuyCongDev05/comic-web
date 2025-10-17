package com.example.projectrestfulapi.util;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.domain.SQL.ComicStats;
import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;
import com.example.projectrestfulapi.mapper.ComicMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ComicUtil {
    private ComicUtil() {
    }

    public static List<ComicResponseDTO.ComicInfoResponseDTO> mapComicsWithRatings(
            List<Comic> comics, List<ComicStats> comicStats) {

        if (comics == null || comicStats == null) {
            throw new IllegalArgumentException("Comic or ComicStats list must not be null");
        }

        if (comics.size() != comicStats.size()) {
            throw new IllegalStateException("Comic and ComicStats lists must have the same size");
        }

        return IntStream.range(0, comics.size())
                .mapToObj(i -> {
                    Comic comic = comics.get(i);
                    BigDecimal rating = comicStats.get(i).getAvgRating();
                    return ComicMapper.mapComicInfoResponseDTO(comic, rating);
                })
                .collect(Collectors.toList());
    }
}
