package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.ComicCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComicCategoryRepository extends JpaRepository<ComicCategory,Long> {
}
