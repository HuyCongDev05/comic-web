package com.example.projectrestfulapi.domain.SQL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class AuthProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String provider;

    @Column(length = 45, nullable = false)
    private String providerAccountId;

    @Column(nullable = false)
    private Instant linkedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private Account account;

    @PrePersist
    private void prePersist() {
        if (linkedAt == null) linkedAt = Instant.now();
    }
}
