package com.sb.studyBoard_Backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nimbusds.oauth2.sdk.Role;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.RoleEnum;
import com.sb.studyBoard_Backend.model.RoleEntity;
import com.sb.studyBoard_Backend.repository.RoleRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum) {
        return roleRepository.findByRoleEnum(roleEnum);
    }

}
