package com.sb.studyBoard_Backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.service.GroupService;
import com.sb.studyBoard_Backend.service.UserService;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.Map;

@AllArgsConstructor
@RestController
public class GroupController {
    private final GroupService groupService;



    @PostMapping("/group")
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
    Group createdGroup = groupService.createGroup(group);
    return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    }
}