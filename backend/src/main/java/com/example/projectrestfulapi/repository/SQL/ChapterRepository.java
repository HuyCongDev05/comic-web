package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    @Query(value = """
            SELECT chapter.* from comic
            inner join chapter on comic.id = chapter.comic_id
            where comic.id = :comicId
            order by chapter.chapter""", nativeQuery = true)
    List<Chapter> getChapterByComicId(@Param("comicId") Long comicId);

    @Query(value = """
            SELECT COUNT(*)
            FROM chapter c
            WHERE c.comic_id = (
              SELECT comic_id\s
              FROM chapter\s
              WHERE uuid_chapter = :uuidChapter
            );""", nativeQuery = true)
    Long getTotalChapterByUuidChapter(@Param("uuidChapter") String uuidChapter);

    @Query(value = "select chapter from chapter where uuid_chapter = :uuidChapter", nativeQuery = true)
    BigDecimal getChapterByUuid(@Param("uuidChapter") String uuidChapter);

    @Query(value = """
            SELECT c.origin_name
            FROM comic c
            JOIN chapter ch ON c.id = ch.comic_id
            WHERE ch.uuid_chapter = :uuidChapter;""", nativeQuery = true)
    String getOriginNameComicByUuidChapter(@Param("uuidChapter") String uuidChapter);
}
