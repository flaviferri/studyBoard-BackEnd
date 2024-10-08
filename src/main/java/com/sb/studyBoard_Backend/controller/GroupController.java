package com.sb.studyBoard_Backend.controller;

import java.util.List;
import java.util.Optional;
import com.sb.studyBoard_Backend.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

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

}