package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Evaluate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluateRepository extends JpaRepository<Evaluate, Long> {
    @Query(value = "SELECT * FROM comic.evaluate where comic_id = :comicId and account_id = :accountId", nativeQuery = true)
    Evaluate findByComicIdAndAccountId(@Param("") Long comicId, @Param("accountId") Long accountId);

}