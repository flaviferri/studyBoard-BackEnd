package com.sb.studyBoard_Backend.controller;


import java.util.List;

import com.sb.studyBoard_Backend.model.*;
import com.sb.studyBoard_Backend.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/add")
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        // Buscar el usuario por userId
        String username = authService.getAuthenticatedUsername();
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Establecer el usuario como creador del grupo
        group.setCreatedBy(user);

        if (group.getBoards() != null) {
            for (Board board : group.getBoards()) {
                board.setGroup(group);
                board.setCreatedBy(user);
            }
        }

        // Guardar el grupo en la base de datos
        Group createdGroup = groupService.createGroup(group);

        // RoleEnum createdRole = roleService.findOrCreateGroupRole("CREATED", createdGroup);
        // userService.assignRoleToUser(user, createdRole);

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
    

    // @PostMapping("/group")
    // public ResponseEntity<Group> createGroup(@RequestBody Group group) {
    // Group createdGroup = groupService.createGroup(group);
    // return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    // }
}