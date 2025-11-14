package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.TotalVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TotalVisitRepository extends JpaRepository<TotalVisit, Long> {
    Optional<TotalVisit> findByDay(LocalDate day);
}
