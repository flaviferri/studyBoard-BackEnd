package com.sb.studyBoard_Backend.repository;

import com.sb.studyBoard_Backend.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {


}
