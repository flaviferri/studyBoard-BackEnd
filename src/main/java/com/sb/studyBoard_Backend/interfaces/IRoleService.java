package com.sb.studyBoard_Backend.interfaces;

import com.sb.studyBoard_Backend.model.RoleEntity;
import com.sb.studyBoard_Backend.model.RoleEnum;

import java.util.Optional;

public interface IRoleService {
    Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum);
}
