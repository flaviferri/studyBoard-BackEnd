package com.sb.studyBoard_Backend.controller;


import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.sb.studyBoard_Backend.model.*;
import com.sb.studyBoard_Backend.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.sb.studyBoard_Backend.model.RoleEntity;
import com.sb.studyBoard_Backend.model.RoleEnum;
import com.sb.studyBoard_Backend.dto.GroupDTO;
import com.sb.studyBoard_Backend.dto.PermissionDTO;
import com.sb.studyBoard_Backend.dto.RoleDTO;
import com.sb.studyBoard_Backend.dto.UserDTO;
import com.sb.studyBoard_Backend.dto.UserGroupRoleDTO;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.Permission;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.model.UserGroupRole;
import com.sb.studyBoard_Backend.service.GroupService;
import com.sb.studyBoard_Backend.service.RoleService;
import com.sb.studyBoard_Backend.service.UserGroupRoleService;
import com.sb.studyBoard_Backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@AllArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;
    private final AuthService authService;
    private final UserService userService;
    private final RoleService roleService;
    private final UserGroupRoleService userGroupRoleService;

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO) {

    @PostMapping("/add")
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        // Buscar el usuario por userId
        String username = authService.getAuthenticatedUsername();
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Establecer el usuario como creador del grupo
        Group group = new Group();
        group.setGroupName(groupDTO.getGroupName());
        group.setCreatedBy(user);

        if (group.getBoards() != null) {
            for (Board board : group.getBoards()) {
                board.setGroup(group);
                board.setCreatedBy(user);
            }
        }

        // Guardar el grupo en la base de datos
        Group createdGroup = groupService.createGroup(group);

        RoleEntity createdRole = roleService.findByRoleEnum(RoleEnum.CREATED)
            .orElseThrow(() -> new RuntimeException("CREATED role not found"));

        UserGroupRole userGroupRole = new UserGroupRole();
        userGroupRole.setUser(user);
        userGroupRole.setGroup(createdGroup);
        userGroupRole.setRole(createdRole);
        userGroupRoleService.save(userGroupRole);

        
        // Retornar la respuesta con el grupo creado
        GroupDTO responseDTO = convertToDTO(createdGroup);
        System.out.println(responseDTO); 

        return ResponseEntity.ok(responseDTO);
    }

    private GroupDTO convertToDTO(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setGroupName(group.getGroupName());
        dto.setCreatedby(convertToDTO(group.getCreatedBy()));

        List<UserGroupRoleDTO> userGroupRoleDTOs = group.getUserGroupRoles().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        dto.setUserGroupRoles(userGroupRoleDTOs);

        return dto;
        
    }

    private UserDTO convertToDTO(UserEntity user) {
        System.out.println("User roles before conversion: " + user.getRoles());

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setGithub(user.getGithubId());
        dto.setEnabled(user.isEnabled());

        List<RoleDTO> roleDTOs = (user.getRoles() != null && !user.getRoles().isEmpty()) ? 
        user.getRoles().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()) : 
        new ArrayList<>();

        dto.setRoles(roleDTOs);

        return dto;
    }

    private RoleDTO convertToDTO(RoleEntity role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setRoleEnum(role.getRoleEnum().name()); 

        List<PermissionDTO> permissionsDTO = (role.getPermissions() != null && !role.getPermissions().isEmpty()) ? 
        role.getPermissions().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()) : 
        new ArrayList<>();
        dto.setPermissions(permissionsDTO);

        return dto;
    }

    private PermissionDTO convertToDTO(Permission permission) {
        return new PermissionDTO(permission.getId(), permission.getName());
    }

    private UserGroupRoleDTO convertToDTO(UserGroupRole userGroupRole) {
        UserGroupRoleDTO dto = new UserGroupRoleDTO();
        dto.setId(userGroupRole.getId());

        if (userGroupRole.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userGroupRole.getUser().getId());
            userDTO.setName(userGroupRole.getUser().getName());
            userDTO.setEmail(userGroupRole.getUser().getEmail());

            dto.setUser(userDTO);
        }

        if (userGroupRole.getRole() != null) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(userGroupRole.getRole().getId());
            roleDTO.setRoleEnum(userGroupRole.getRole().getRoleEnum().name());

            dto.setRole(roleDTO);
        }

        return dto;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        Group group = groupService.getGroupById(id)
            .orElseThrow(() -> new RuntimeException("Group not found"));
            return ResponseEntity.ok(group);
    }
    
}