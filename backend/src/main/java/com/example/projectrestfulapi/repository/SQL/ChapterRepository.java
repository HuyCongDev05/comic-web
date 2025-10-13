package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    @Query(value = """
            SELECT chapter.* from comic
            inner join chapter on comic.id = chapter.comic_id
            where comic.id = :comicId
            order by chapter.chapter""", nativeQuery = true)
    List<Chapter> getChapterByComicId(@Param("comicId") Long comicId);
}
