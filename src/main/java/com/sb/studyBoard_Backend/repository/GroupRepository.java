package com.sb.studyBoard_Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.studyBoard_Backend.model.Group;

@Repository
public interface GroupRepository extends JpaRepository <Group, Long> {
    
}
