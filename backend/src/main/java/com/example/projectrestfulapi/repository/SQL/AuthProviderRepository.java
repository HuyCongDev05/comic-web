package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
    List<AuthProvider> findByAccountIdIs(Long accountId);

    boolean existsByProviderAccountId(String providerAccountId);

    @Query(value = "select account_id from auth_provider where provider_account_id = :providerAccountId", nativeQuery = true)
    Long findAccountIdByProviderAccountId(@Param("providerAccountId") String providerAccountId);
}
