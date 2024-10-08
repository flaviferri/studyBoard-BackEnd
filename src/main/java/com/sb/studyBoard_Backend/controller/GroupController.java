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

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        // Buscar el usuario por su nombre de usuario
        String username = authService.getAuthenticatedUsername();
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Asignar el usuario como creador del grupo
        group.setCreatedBy(user);

        // Llamar al servicio para crear el grupo
        Group createdGroup = groupService.createGroup(group, user);

        // Retornar la respuesta con el grupo creado
        return ResponseEntity.ok(createdGroup);
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