package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.ComicCategories;
import com.example.projectrestfulapi.dto.response.Dashboard.DashboardResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComicCategoriesRepository extends JpaRepository<ComicCategories, Long> {
    @Query(value = """
            SELECT 
                c.name AS categoryName,
                ROUND(
                    COUNT(cc.categories_id) * 100.0 /
                    (SELECT COUNT(*) FROM comic_categories),
                2) AS ratio
            FROM categories c
            JOIN comic_categories cc
                ON c.id = cc.categories_id
            GROUP BY c.name
            ORDER BY ratio DESC""", nativeQuery = true)
    List<DashboardResponseDTO.HomeDashboard.CategoriesRatio> getCategoriesRatio();

}
