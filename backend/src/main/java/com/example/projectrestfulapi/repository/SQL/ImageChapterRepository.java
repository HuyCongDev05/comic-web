package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.ImageChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageChapterRepository extends JpaRepository<ImageChapter, Long> {
    @Query(value = """
            SELECT * FROM comic.image_chapter
            where uuid_chapter = :uuidChapter
            order by image_number;""", nativeQuery = true)
    List<ImageChapter> getImageChapterByUuidChapter(@Param("uuidChapter") String uuidChapter);
}
