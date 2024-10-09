package com.sb.studyBoard_Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "postits")
public class Postit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate date;
  
    @Column(columnDefinition = "TEXT")
    private String textContent;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "board_id")
    @JsonBackReference
    private Board board;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(updatable = false, name = "created_by", nullable = false)
    private UserEntity createdBy;
}
