package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.dto.response.Dashboard.DashboardResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {

    Page<Comic> findByStatusOrderByUpdateTimeDesc(String status, Pageable pageable);

    Page<Comic> findByStatusAndLastChapterLessThanEqualOrderByUpdateTimeDesc(String status, BigDecimal lastChapter, Pageable pageable);

    @Query(
            value = """
                        SELECT * FROM comic 
                        WHERE name LIKE CONCAT('%', :keyword, '%')
                        ORDER BY update_time DESC
                    """,
            countQuery = """
                        SELECT COUNT(*) FROM comic 
                        WHERE name LIKE CONCAT('%', :keyword, '%')
                    """,
            nativeQuery = true
    )
    Page<Comic> findComicByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(
            value = """
                        SELECT comic.* FROM comic
                        INNER JOIN comic_categories cc ON comic.id = cc.comic_id
                        INNER JOIN categories c ON c.id = cc.categories_id
                        WHERE c.origin_name = :categories
                        ORDER BY comic.update_time DESC
                    """,
            countQuery = """
                        SELECT COUNT(*) FROM comic
                        INNER JOIN comic_categories cc ON comic.id = cc.comic_id
                        INNER JOIN categories c ON c.id = cc.categories_id
                        WHERE c.origin_name = :categories
                    """,
            nativeQuery = true
    )
    Page<Comic> findComicByCategories(@Param("categories") String categories, Pageable pageable);

    @Query(
            value = """
                    SELECT uuid_comic as uuid, name, origin_name as originName, image_link as poster, intro, last_chapter as lastChapter,
                                               status, Date(update_time) as updated, sum(comic_daily_views.views) as views FROM comic.comic
                    inner join comic_daily_views on comic.id = comic_daily_views.comic_id
                    group by comic.id;
                    """,
            countQuery = """
                    SELECT count(*) FROM comic.comic;
                    """,
            nativeQuery = true
    )
    Page<DashboardResponseDTO.ComicsDashboard.Comics> findAllComics(Pageable pageable);

    Optional<Comic> findComicByUuidComic(String uuidComic);

    Optional<Comic> getComicByOriginName(String OriginName);

    boolean existsByUuidComic(String uuidComic);

    List<Comic> findByUuidComicIn(List<String> comicUuids);

    @Query("SELECT count(c) FROM Comic c WHERE c.status = :status")
    Long getCountComicByStatus(String status);
}
