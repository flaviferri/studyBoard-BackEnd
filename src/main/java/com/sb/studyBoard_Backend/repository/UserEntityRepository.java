package com.sb.studyBoard_Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.studyBoard_Backend.model.UserEntity;

public interface UserEntityRepository extends JpaRepository <UserEntity, Long> {
    
}
