package com.example.projectrestfulapi.domain.SQL;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "avatar_url")
    private String avatar;

    @Column(name = "facebook_url")
    private String facebook;

    @Column(name = "instagram_url")
    private String instagram;

    @Column(name = "x_url")
    private String x;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "member_position",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id")
    )
    private List<Position> positions  = new ArrayList<>();
}
