// package com.sb.studyBoard_Backend.model;

// import java.util.HashSet;
// import java.util.Set;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.Table;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// @Entity
// @Getter
// @Setter
// @NoArgsConstructor
// @Table(name = "boards")
// public class Board extends BaseEntity {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;

// @ManyToOne
// @JoinColumn(name = "group_id")
// private Group group;

// @Column(nullable = false)
// private String title;

// @ManyToOne
// @JoinColumn(updatable = false, name = "created_by", nullable = false)
// private UserEntity createdBy;

// @OneToMany(mappedBy = "board")
// private Set<Postit> postits = new HashSet<>();

// }
