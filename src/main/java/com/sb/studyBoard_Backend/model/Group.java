package com.sb.studyBoard_Backend.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "groups")
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String groupName;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(updatable = false, name = "created_by", nullable = false)
    private UserEntity createdBy;

    @OneToMany(mappedBy = "group")
    private Set<UserGroupRole> userGroupRoles = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<Board> boards = new HashSet<>();
}
