package com.example.projectrestfulapi.domain.SQL;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 255, unique = true)
    private String name;

    @Column(name = "origin_name", length = 255, unique = true)
    private String originName;

    @Column(name = "detail", length = 500)
    private String detail;
}
