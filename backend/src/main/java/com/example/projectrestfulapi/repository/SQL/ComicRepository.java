package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.dto.response.Dashboard.DashboardResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
        SELECT 
            comic.uuid_comic AS uuid,
            comic.name,
            comic.origin_name AS originName,
            comic.image_link AS poster,
            comic.intro,
            comic.last_chapter AS lastChapter,
            comic.status,
            DATE(comic.update_time) AS updated,
            COALESCE(SUM(comic_daily_views.views), 0) AS views
        FROM comic.comic
        LEFT JOIN comic_daily_views 
            ON comic.id = comic_daily_views.comic_id
        GROUP BY 
            comic.id,
            comic.uuid_comic,
            comic.name,
            comic.origin_name,
            comic.image_link,
            comic.intro,
            comic.last_chapter,
            comic.status,
            comic.update_time;
    """,
            countQuery = """
        SELECT COUNT(DISTINCT comic.id) 
        FROM comic.comic
        LEFT JOIN comic_daily_views 
            ON comic.id = comic_daily_views.comic_id
    """,
            nativeQuery = true
    )
    Page<DashboardResponseDTO.ComicsDashboard.Comics> findAllComics(Pageable pageable);

    @Query(
            value = """
        SELECT 
            comic.uuid_comic AS uuid,
            comic.name,
            comic.origin_name AS originName,
            comic.image_link AS poster,
            comic.intro,
            comic.last_chapter AS lastChapter,
            comic.status,
            DATE(comic.update_time) AS updated,
            COALESCE(SUM(comic_daily_views.views), 0) AS views
        FROM comic.comic
        LEFT JOIN comic_daily_views 
            ON comic.id = comic_daily_views.comic_id
        WHERE comic.status = :status
        GROUP BY 
            comic.id,
            comic.uuid_comic,
            comic.name,
            comic.origin_name,
            comic.image_link,
            comic.intro,
            comic.last_chapter,
            comic.status,
            comic.update_time;
    """,
            countQuery = """
        SELECT COUNT(DISTINCT comic.id) 
        FROM comic.comic
        LEFT JOIN comic_daily_views 
            ON comic.id = comic_daily_views.comic_id
        where comic.status = :status
    """,
            nativeQuery = true
    )
    Page<DashboardResponseDTO.ComicsDashboard.Comics> findComicsByStatus(@Param("status") String status, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = """
        update comic
        set status = :status
        where uuid_comic = :uuidComics;
    """, nativeQuery = true)
    void updateSatusComics(@Param("uuidComics") String uuidComics, @Param("status") String status);

    Optional<Comic> findComicByUuidComic(String uuidComic);

    Optional<Comic> getComicByOriginName(String OriginName);

    boolean existsByUuidComic(String uuidComic);

    List<Comic> findByUuidComicIn(List<String> comicUuids);

    @Query("SELECT count(c) FROM Comic c WHERE c.status = :status")
    Long getCountComicByStatus(String status);
}
