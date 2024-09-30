package com.sb.studyBoard_Backend.repositorie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.studyBoard_Backend.model.Group;

@Repository
public interface GroupRepositorie extends JpaRepository <Group, Long> {
    
}
