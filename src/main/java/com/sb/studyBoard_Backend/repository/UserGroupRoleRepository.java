package com.sb.studyBoard_Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.studyBoard_Backend.model.UserGroupRole;

public interface UserGroupRoleRepository extends JpaRepository<UserGroupRole, Long> {
    
}
