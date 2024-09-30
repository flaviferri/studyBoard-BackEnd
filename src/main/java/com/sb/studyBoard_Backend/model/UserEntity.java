package com.sb.studyBoard_Backend.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@Builder
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = true, unique = true)
    private String githubId;

    @Column(nullable = true)
    private String password;

    @Column
    private String avatarUrl;

    @Column(nullable = false)
    private boolean enabled;

    @OneToMany(mappedBy = "user")
    private Set<UserGroupRole> userGroupRoles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @Builder
    public UserEntity(String name, String email, String githubId, String password, String avatarUrl, boolean enabled,
            Collection<Role> roles) {
        this.name = name;
        this.email = email;
        this.githubId = githubId;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.enabled = enabled;
        this.roles = roles;
    }
}
