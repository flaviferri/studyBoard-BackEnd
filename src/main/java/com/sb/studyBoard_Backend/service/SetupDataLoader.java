package com.sb.studyBoard_Backend.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sb.studyBoard_Backend.model.Permission;
import com.sb.studyBoard_Backend.model.Role;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.PermissionRepository;
import com.sb.studyBoard_Backend.repository.RoleRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.lang.NonNull;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        Permission adminPermission = createPermissionIfNotFound("ADMIN_PERMISSION");
        Permission readPostit = createPermissionIfNotFound("READ_POSTIT");
        Permission writePostit = createPermissionIfNotFound("WRITE_POSTIT");
        Permission deletePostit = createPermissionIfNotFound("DELETE_POSTIT");
        Permission updatePostit = createPermissionIfNotFound("UPDATE_POSTIT");
        Permission readGroup = createPermissionIfNotFound("READ_GROUP");
        Permission createGroup = createPermissionIfNotFound("CREATE_GROUP");
        Permission deleteGroup = createPermissionIfNotFound("DELETE_GROUP");
        Permission updateGroup = createPermissionIfNotFound("UPDATE_GROUP");

        List<Permission> adminPermissions = Arrays.asList(readPostit, writePostit, deletePostit, updatePostit,
                adminPermission, readGroup, createGroup, deleteGroup, updateGroup);
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPermissions);

        UserEntity user = UserEntity.builder()
                .name("TestAdmin")
                .password(passwordEncoder.encode("test"))
                .email("testadmin@test.com")
                .roles(Arrays.asList(adminRole))
                .enabled(true)
                .build();

        System.out.println("Creando usuario administrador...");
        userRepository.save(user);
        System.out.println("Usuario administrador creado con éxito.");

        alreadySetup = true;
    }

    @Transactional
    Permission createPermissionIfNotFound(String name) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> {
                    Permission permission = new Permission(name);
                    permissionRepository.save(permission);
                    return permission;
                });
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Permission> permissions) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role(name, permissions);
                    roleRepository.save(role);
                    return role;
                });
    }
}
