package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = """
            SELECT category.* FROM comic
                inner join comic_category on comic.id = comic_category.comic_id
                inner join category on comic_category.category_id = category.id
                where comic.id = :comicId""", nativeQuery = true)
    List<Category> getCategoryByComicId(@Param("comicId") Long comicId);
}
