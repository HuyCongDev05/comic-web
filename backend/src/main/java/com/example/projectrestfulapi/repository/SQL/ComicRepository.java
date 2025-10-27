package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
    @Query(value = "select * from comic where status = 'đang cập nhật' order by update_time desc limit 21 offset :offset", nativeQuery = true)
    List<Comic> getNewUpdateComic(@Param("offset") int offset);

    @Query(value = "select * from comic where status = 'đã hoàn thành' order by update_time desc limit 21 offset :offset", nativeQuery = true)
    List<Comic> getCompletedComic(@Param("offset") int offset);

    @Query(value = "select * from comic where status = 'đang cập nhật' and last_chapter <= 15 order by update_time desc limit 21 offset :offset", nativeQuery = true)
    List<Comic> getNewComic(@Param("offset") int offset);

    @Query(value = "SELECT * FROM comic WHERE name LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<Comic> findComicByKeyword(@Param("keyword") String keyword);

    @Query(value = """
            SELECT comic.* FROM comic
            inner join comic_categories on comic.id = comic_categories.comic_id
            inner join categories on categories.id = comic_categories.categories_id
            where categories.origin_name = :categories
            order by comic.update_time desc limit 24 offset :offset;""", nativeQuery = true)
    List<Comic> getComicByCategory(@Param("categories") String categories, @Param("offset") int offset);

    Optional<Comic> findComicByUuidComic(String uuidComic);

    Optional<Comic> getComicByOriginName(String OriginName);

    boolean existsByUuidComic(String uuidComic);
}
