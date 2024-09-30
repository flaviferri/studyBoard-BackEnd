package com.sb.studyBoard_Backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.UserEntityRepository;
import com.sb.studyBoard_Backend.service.GroupService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class GroupController {

    private GroupService groupService;
    private UserEntityRepository userEntityRepository;

    public GroupController(GroupService groupService, UserEntityRepository userEntityRepository) {
        this.groupService = groupService;
        this.userEntityRepository = userEntityRepository;
    }

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/group")
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        // Suponiendo que el id del usuario genérico es 1
        UserEntity anonymousUser = userEntityRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Usuario genérico no encontrado"));

        // Asignar el usuario genérico como el creador del grupo
        group.setCreatedBy(anonymousUser);

        Group createdGroup = groupService.createGroup(group);
        return ResponseEntity.ok(createdGroup);
    }
}
