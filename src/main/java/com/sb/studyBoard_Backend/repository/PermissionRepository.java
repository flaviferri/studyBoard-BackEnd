package com.sb.studyBoard_Backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sb.studyBoard_Backend.model.PermissionEntity;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    Optional<PermissionEntity> findByName(String name);
}
