package com.sb.studyBoard_Backend.service;

import org.springframework.stereotype.Service;

import com.nimbusds.oauth2.sdk.Role;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.repository.RoleRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    // public Role findOrCreateGroupRole(String roleString, Group group) {
    //     return roleRepository;
    // }
    
}
