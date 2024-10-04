package com.sb.studyBoard_Backend.service;

import org.springframework.stereotype.Service;

import com.sb.studyBoard_Backend.model.UserGroupRole;
import com.sb.studyBoard_Backend.repository.UserGroupRoleRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserGroupRoleService {

    private final UserGroupRoleRepository userGroupRoleRepository;

    public UserGroupRole save(UserGroupRole userGroupRole) {
        return userGroupRoleRepository.save(userGroupRole);
    }
    
}
