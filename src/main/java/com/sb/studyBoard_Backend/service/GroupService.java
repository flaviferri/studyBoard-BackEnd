package com.sb.studyBoard_Backend.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.sb.studyBoard_Backend.model.*;
import org.springframework.stereotype.Service;

import com.sb.studyBoard_Backend.repository.GroupRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserGroupRoleService userGroupRoleService;
    private final RoleService roleService;


    @Transactional
    public Group createGroup(Group group, UserEntity user) {
        // Establecer el creador del grupo
        group.setCreatedBy(user);

        // Asignar el usuario a los tableros del grupo, si existen
        if (group.getBoards() != null) {
            for (Board board : group.getBoards()) {
                board.setGroup(group);
                board.setCreatedBy(user);
            }
        }

        // Guardar el grupo en la base de datos
        Group createdGroup = groupRepository.save(group);

        // Buscar el rol CREATED
        RoleEntity createdRole = roleService.findByRoleEnum(RoleEnum.CREATED)
                .orElseThrow(() -> new RuntimeException("CREATED role not found"));

        // Verificar si el usuario ya tiene el rol CREATED
        boolean hasRoleCreated = user.getRoles().stream()
                .anyMatch(role -> role.getRoleEnum() == RoleEnum.CREATED);

        if (!hasRoleCreated) {
            // Agregar el rol CREATED a la colección de roles del usuario
            user.getRoles().add(createdRole);
            // Actualizar el usuario en la base de datos
            userGroupRoleService.saveUser(user); // Asegúrate de tener este método implementado
        } else {
            System.out.println("El usuario ya tiene el rol CREATED.");
        }

        // Crear una nueva relación UserGroupRole
        UserGroupRole userGroupRole = new UserGroupRole();
        userGroupRole.setUser(user);
        userGroupRole.setGroup(createdGroup);
        userGroupRole.setRole(createdRole);

        // Guardar la relación en la base de datos
        userGroupRoleService.save(userGroupRole);

        return createdGroup;
    }


    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }
}
