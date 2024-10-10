package com.sb.studyBoard_Backend.controller;

import com.sb.studyBoard_Backend.dto.GroupDTO;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        List<GroupDTO> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<GroupDTO>> getGroupById(@PathVariable Long id) {
        Optional<GroupDTO> group = groupService.findGroupDTOById(id);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/join/{groupId}")
    public ResponseEntity<GroupDTO> joinGroup(@PathVariable Long groupId) {
        GroupDTO joinedGroup = groupService.joinGroup(groupId);
        return new ResponseEntity<>(joinedGroup, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<GroupDTO>> getUserGroups() {
        List<GroupDTO> userGroups = groupService.getUserGroups();
        return new ResponseEntity<>(userGroups, HttpStatus.OK);
    }
}
