package com.sb.studyBoard_Backend.service;

import java.util.Arrays;
import java.util.Collection;

import com.sb.studyBoard_Backend.model.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sb.studyBoard_Backend.model.Permission;
import com.sb.studyBoard_Backend.model.RoleEntity;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.PermissionRepository;
import com.sb.studyBoard_Backend.repository.RoleRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.lang.NonNull;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

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

        Permission createPostitPermission = createPermissionIfNotFound("CREATE_POSTIT");
        Permission readPostitPermission = createPermissionIfNotFound("READ_POSTIT");
        Permission updatePostitPermission = createPermissionIfNotFound("UPDATE_POSTIT");
        Permission deletePostitPermission = createPermissionIfNotFound("DELETE_POSTIT");
        Permission refactorPermission = createPermissionIfNotFound("REFACTOR");

        Permission readGroup = createPermissionIfNotFound("READ_GROUP");
        Permission createGroup = createPermissionIfNotFound("CREATE_GROUP");
        Permission deleteGroup = createPermissionIfNotFound("DELETE_GROUP");
        Permission updateGroup = createPermissionIfNotFound("UPDATE_GROUP");

        Permission readBoard = createPermissionIfNotFound("READ_BOARD");
        Permission createBoard = createPermissionIfNotFound("CREATE_BOARD");
        Permission deleteBoard = createPermissionIfNotFound("DELETE_BOARD");
        Permission updateBoard = createPermissionIfNotFound("UPDATE_BOARD");

        // Crear roles y asignar permisos
        RoleEntity roleAdmin = createRoleIfNotFound(RoleEnum.ADMIN, Arrays.asList(
                createPostitPermission, readPostitPermission, updatePostitPermission, deletePostitPermission,
                readBoard, createGroup, deleteGroup, updateGroup, readBoard, createBoard, deleteBoard, updateBoard, refactorPermission));

        RoleEntity roleCreated = createRoleIfNotFound(RoleEnum.CREATED, Arrays.asList(
                createBoard));

        RoleEntity roleUser = createRoleIfNotFound(RoleEnum.USER, Arrays.asList(
                createPostitPermission, readPostitPermission, updatePostitPermission, deletePostitPermission, readBoard, readGroup, createGroup));

        // Crear usuario administrador si no existe
        Boolean adminExists = userRepository.findByEmail("testadmin@test.com").isPresent();
        if (!adminExists) {
            UserEntity adminUser = UserEntity.builder()
                    .name("TestAdmin")
                    .password(passwordEncoder.encode("test"))
                    .email("testadmin@test.com")
                    .roles(Arrays.asList(roleAdmin))
                    .enabled(true)
                    .build();

            userRepository.save(adminUser);
        }

        // Marcar como completado
        alreadySetup = true;
    }

    // Métodos de ayuda para crear permisos y roles si no existen
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
    RoleEntity createRoleIfNotFound(RoleEnum roleEnum, Collection<Permission> permissions) {
        return roleRepository.findByRoleEnum(roleEnum)
                .orElseGet(() -> {
                    RoleEntity role = new RoleEntity(roleEnum, permissions);
                    roleRepository.save(role);
                    return role;
                });
    }
}