package com.sb.studyBoard_Backend.repository;

import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Postit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PostItRepository extends JpaRepository<Postit,Long> {
    List<Postit> findAllByBoardId(Long boardId);

    List<Postit> findByBoardAndDate(Board board, LocalDate date);
}
