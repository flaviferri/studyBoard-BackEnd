package com.sb.studyBoard_Backend.service;

import java.util.Arrays;
import java.util.Collection;

import com.sb.studyBoard_Backend.model.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sb.studyBoard_Backend.model.PermissionEntity;
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
        if (alreadySetup) {
            return;
        }

        PermissionEntity createPostitPermission = createPermissionIfNotFound("CREATE_POSTIT");
        PermissionEntity readPostitPermission = createPermissionIfNotFound("READ_POSTIT");
        PermissionEntity updatePostitPermission = createPermissionIfNotFound("UPDATE_POSTIT");
        PermissionEntity deletePostitPermission = createPermissionIfNotFound("DELETE_POSTIT");
        PermissionEntity refactorPermission = createPermissionIfNotFound("REFACTOR");

        PermissionEntity readGroup = createPermissionIfNotFound("READ_GROUP");
        PermissionEntity createGroup = createPermissionIfNotFound("CREATE_GROUP");
        PermissionEntity deleteGroup = createPermissionIfNotFound("DELETE_GROUP");
        PermissionEntity updateGroup = createPermissionIfNotFound("UPDATE_GROUP");

        PermissionEntity readBoard = createPermissionIfNotFound("READ_BOARD");
        PermissionEntity createBoard = createPermissionIfNotFound("CREATE_BOARD");
        PermissionEntity deleteBoard = createPermissionIfNotFound("DELETE_BOARD");
        PermissionEntity updateBoard = createPermissionIfNotFound("UPDATE_BOARD");

        RoleEntity roleAdmin = createRoleIfNotFound(RoleEnum.ADMIN, Arrays.asList(
                createPostitPermission,
                readPostitPermission,
                updatePostitPermission,
                deletePostitPermission,
                readBoard,
                createGroup,
                deleteGroup,
                updateGroup,
                createBoard,
                deleteBoard,
                updateBoard,
                refactorPermission));

        RoleEntity roleCreated = createRoleIfNotFound(RoleEnum.CREATED, Arrays.asList(createBoard));

        RoleEntity roleUser = createRoleIfNotFound(RoleEnum.USER, Arrays.asList(
                createPostitPermission,
                readPostitPermission,
                updatePostitPermission,
                deletePostitPermission,
                readBoard,
                readGroup,
                createGroup));

        // Crear usuario administrador si no existe
        if (!userRepository.findByEmail("testadmin@test.com").isPresent()) {
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
    PermissionEntity createPermissionIfNotFound(String name) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> {
                    PermissionEntity permission = new PermissionEntity(name);
                    permissionRepository.save(permission);
                    return permission;
                });
    }

    @Transactional
    RoleEntity createRoleIfNotFound(RoleEnum roleEnum, Collection<PermissionEntity> permissionsEntity) {
        return roleRepository.findByRoleEnum(roleEnum)
                .orElseGet(() -> {
                    RoleEntity role = new RoleEntity(roleEnum, permissionsEntity);
                    roleRepository.save(role);
                    return role;
                });
    }
}