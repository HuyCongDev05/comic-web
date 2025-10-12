package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    Optional<Account> findByUuid(String uuid);

    boolean existsByUsername(String username);

    boolean existsByUuid(String uuid);

    void deleteAllByUuid(String uuid);

    @Query(value = "select uuid from account where user_id = :userId", nativeQuery = true)
    String findUuidByUserId(@Param("userId") Long userId);
}
