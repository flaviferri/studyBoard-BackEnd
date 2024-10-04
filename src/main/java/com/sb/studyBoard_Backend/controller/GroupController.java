package com.sb.studyBoard_Backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.service.GroupService;
import com.sb.studyBoard_Backend.service.UserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<Group> createGroup(@PathVariable Long userId, @RequestBody Group group) {
        // Buscar el usuario por userId
        UserEntity user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Establecer el usuario como creador del grupo
        group.setCreatedBy(user);

        // Guardar el grupo en la base de datos
        Group createdGroup = groupService.createGroup(group);

        // Retornar la respuesta con el grupo creado
        return ResponseEntity.ok(createdGroup);
    }

    // @PostMapping("/group")
    // public ResponseEntity<Group> createGroup(@RequestBody Group group) {
    // Group createdGroup = groupService.createGroup(group);
    // return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    // }
}