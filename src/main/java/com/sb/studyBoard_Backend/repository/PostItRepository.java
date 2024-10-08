package com.sb.studyBoard_Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.studyBoard_Backend.model.Postit;

public interface PostItRepository extends JpaRepository<Postit, Long> {
    
}
