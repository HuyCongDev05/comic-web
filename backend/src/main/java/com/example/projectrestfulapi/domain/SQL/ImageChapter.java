package com.example.projectrestfulapi.domain.SQL;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "image_chapter")
public class ImageChapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "image_number")
    private Integer imageNumber;

    @ManyToOne
    @JoinColumn(name = "uuid_chapter")
    private Chapter chapter;
}
