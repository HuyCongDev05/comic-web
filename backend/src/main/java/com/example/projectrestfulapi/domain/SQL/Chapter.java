package com.example.projectrestfulapi.domain.SQL;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "chapter")
public class Chapter {
    @Id
    @Column(name = "uuid_chapter", length = 36)
    private String uuidChapter;

    @Column(precision = 6, scale = 1)
    private BigDecimal chapter;

    private Instant time;

    @ManyToOne
    @JoinColumn(name = "uuid_comic")
    private Comic comic;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageChapter> imageChapters;
}
