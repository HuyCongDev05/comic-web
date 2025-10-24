package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByStatus(String status);

    @Query(value = """
            SELECT status.status FROM status
            inner join account on account.status_id = status.id
            where account.uuid = :accountUuid""", nativeQuery = true)
    String findStatusByUuidAccount(@Param("accountUuid") String accountUuid);
}
