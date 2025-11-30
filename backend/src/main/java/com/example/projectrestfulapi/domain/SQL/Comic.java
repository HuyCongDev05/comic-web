package com.example.projectrestfulapi.domain.SQL;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "comic")
public class Comic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid_comic", nullable = false, unique = true, length = 36)
    private String uuidComic;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "origin_name", nullable = false)
    private String originName;

    @Column(name = "image_link", nullable = false)
    private String imageLink;

    @Column(name = "intro", length = 1000)
    private String intro;

    @Column(name = "last_chapter", precision = 6, scale = 1)
    private BigDecimal lastChapter;

    @Column(name = "status")
    private String status;

    @Column(name = "update_time", nullable = false)
    private Instant updateTime;
}
