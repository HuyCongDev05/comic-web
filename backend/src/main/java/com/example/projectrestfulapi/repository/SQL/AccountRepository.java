package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(value = "select id from account where uuid = :uuid", nativeQuery = true)
    Long findAccountIdByUuid(@Param("uuid") String uuid);

    @Modifying
    @Transactional
    @Query(value = "UPDATE account a SET a.avatar = :avatar WHERE a.uuid = :uuid", nativeQuery = true)
    void updateAvatarByUuid(@Param("uuid") String uuid, @Param("avatar") String avatar);

    @Query(value = "SELECT avatar FROM account where uuid = :uuid", nativeQuery = true)
    String findAvatarByUuid(@Param("uuid") String uuid);
}
