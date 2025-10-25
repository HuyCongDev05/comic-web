package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.AccountFollowComic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountFollowComicRepository extends JpaRepository<AccountFollowComic, Long> {
    List<AccountFollowComic> findByAccountId(Long accountId);
    boolean existsByAccountIdAndComicId(Long accountId, Long comicId);
    void deleteByAccountIdAndComicId(Long accountId, Long comicId);
}
