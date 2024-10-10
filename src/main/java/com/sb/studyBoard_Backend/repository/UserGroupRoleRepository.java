package com.sb.studyBoard_Backend.repository;

import com.sb.studyBoard_Backend.model.UserGroupRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRoleRepository extends JpaRepository<UserGroupRole, Long> {
    
}
