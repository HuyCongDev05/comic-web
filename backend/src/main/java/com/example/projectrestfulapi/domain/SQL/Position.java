package com.example.projectrestfulapi.domain.SQL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "position")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position_name")
    private String positionName;

    @ManyToMany(mappedBy = "positions")
    @JsonIgnore
    @Column(nullable = false)
    private List<Member> members = new ArrayList<>();
}
