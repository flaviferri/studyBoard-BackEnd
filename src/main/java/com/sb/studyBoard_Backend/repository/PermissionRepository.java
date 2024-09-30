package com.sb.studyBoard_Backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sb.studyBoard_Backend.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByName(String name);
}
