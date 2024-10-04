package com.sb.studyBoard_Backend.repository;


import com.sb.studyBoard_Backend.model.Postit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostitRepository extends JpaRepository<Postit, Long> {
}