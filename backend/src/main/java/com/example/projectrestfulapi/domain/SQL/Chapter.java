package com.example.projectrestfulapi.domain.SQL;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "chapter")
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "uuid_chapter", nullable = false, length = 36, unique = true)
    private String uuidChapter;

    @Column(name = "chapter", precision = 6, scale = 1)
    private BigDecimal chapter;

    @Column(name = "time")
    private Instant time;

    @ManyToOne
    @JoinColumn(name = "comic_id")
    private Comic comic;
}
