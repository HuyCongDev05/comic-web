package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.AccountFollowComic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountFollowComicRepository extends JpaRepository<AccountFollowComic, Long> {
    Page<AccountFollowComic> findByAccountId(Long accountId, Pageable pageable);

    boolean existsByAccountIdAndComicId(Long accountId, Long comicId);

    void deleteByAccountIdAndComicId(Long accountId, Long comicId);

    Long countComicByComicId(Long comicId);
}
