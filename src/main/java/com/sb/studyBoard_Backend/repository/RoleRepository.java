package com.sb.studyBoard_Backend.repository;

import java.util.Optional;

import com.sb.studyBoard_Backend.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.studyBoard_Backend.model.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

   /* Optional<Role> findByName(String name);*/

    Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum);


}

