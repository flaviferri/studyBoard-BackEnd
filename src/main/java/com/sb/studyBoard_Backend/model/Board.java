package com.sb.studyBoard_Backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "boards")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String color;

    @ManyToOne
    @JoinColumn(updatable = false, name = "created_by", nullable = false)
    @JsonIgnore
    private UserEntity createdBy;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @JsonManagedReference
    private Set<Postit> postits = new HashSet<>();

}
