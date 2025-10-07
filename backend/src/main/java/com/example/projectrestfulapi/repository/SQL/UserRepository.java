package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    boolean existsById(Long id);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    void deleteAllById(Long id);
}
