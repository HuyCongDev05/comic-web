package com.example.projectrestfulapi.domain.SQL;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "comic_daily_views")
public class ComicDailyViews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comic_id", nullable = false)
    private Comic comic;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Long views;
}

