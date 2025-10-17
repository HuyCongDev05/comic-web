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
    @Query(value = "select * from comic where status = 'đang cập nhật' order by update_time desc limit 20 offset :offset", nativeQuery = true)
    List<Comic> getNewUpdateComic(@Param("offset") int offset);

    @Query(value = "select * from comic where status = 'đã hoàn thành' order by update_time desc limit 20 offset :offset", nativeQuery = true)
    List<Comic> getCompletedComic(@Param("offset") int offset);

    @Query(value = "select * from comic where status = 'đang cập nhật' and last_chapter <= 15 order by update_time desc limit 20 offset :offset", nativeQuery = true)
    List<Comic> getNewComic(@Param("offset") int offset);

    Optional<Comic> getComicByOriginName(String OriginName);

    @Query(value = "SELECT * FROM comic WHERE name LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<Comic> findComicByKeyword(@Param("keyword") String keyword);

    @Query(value = """
            SELECT comic.* FROM comic
            inner join comic_category on comic.id = comic_category.comic_id
            inner join category on category.id = comic_category.category_id
            where category.origin_name = :category
            order by comic.update_time desc limit 15 offset :offset;""", nativeQuery = true)
    List<Comic> getComicByCategory(@Param("category") String category, @Param("offset") int offset);

    Long findComicIdByUuidComic(String uuidComic);
    Optional<Comic> findComicByUuidComic(String uuidComic);
}
