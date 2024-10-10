package com.sb.studyBoard_Backend.interfaces;

import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.model.UserGroupRole;

public interface IUserGroupRoleService {
    UserGroupRole save(UserGroupRole userGroupRole);
    void saveUser(UserEntity user);
}