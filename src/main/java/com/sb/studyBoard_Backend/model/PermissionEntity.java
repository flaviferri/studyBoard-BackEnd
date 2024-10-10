package com.sb.studyBoard_Backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions")
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "permissionsEntity")
    private Collection<RoleEntity> roles;

    @Builder
    public PermissionEntity(String name) {
        this.name = name;
    }
}

