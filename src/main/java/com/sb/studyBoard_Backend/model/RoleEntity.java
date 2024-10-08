package com.sb.studyBoard_Backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")

public class RoleEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;

    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users;

    @ManyToMany
    @JoinTable(name = "roles_permissions", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Collection<PermissionEntity> permissionsEntity;

    @OneToMany(mappedBy = "role")
    private Set<UserGroupRole> userGroupRoles = new HashSet<>();

    public RoleEntity(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
    }

    @Builder
    public RoleEntity(RoleEnum roleEnum, Collection<PermissionEntity> permissionsEntity) {
        this.roleEnum = roleEnum;
        this.permissionsEntity = permissionsEntity;
    }
}
