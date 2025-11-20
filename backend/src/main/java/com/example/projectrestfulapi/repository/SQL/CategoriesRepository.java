package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    @Query(value = """
            SELECT categories.* FROM comic
                inner join comic_categories on comic.id = comic_categories.comic_id
                inner join categories on comic_categories.categories_id = categories.id
                where comic.id = :comicId""", nativeQuery = true)
    List<Categories> getCategoriesByComicId(@Param("comicId") Long comicId);
}