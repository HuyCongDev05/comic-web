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
@Table(name = "comic")
public class Comic {
    @Id
    @Column(name = "uuid_comic", length = 36)
    private String uuidComic;

    @Column(name = "id", unique = true, insertable = false, updatable = false)
    private Integer id;

    private String name;

    @Column(name = "origin_name")
    private String originName;

    @Column(name = "image_link")
    private String imageLink;

    @Column(columnDefinition = "TEXT")
    private String intro;

    @Column(name = "last_chapter", precision = 6, scale = 1)
    private BigDecimal lastChapter;

    private String status;

    @Column(name = "update_time")
    private Instant updateTime;

    @OneToMany(mappedBy = "comic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters;

    @OneToMany(mappedBy = "comic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComicCategory> comicCategories;
}
