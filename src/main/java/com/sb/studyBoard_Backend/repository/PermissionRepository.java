package com.sb.studyBoard_Backend.repository;

import com.sb.studyBoard_Backend.model.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    Optional<PermissionEntity> findByName(String name);
}
