package com.sb.studyBoard_Backend.repository;

import com.sb.studyBoard_Backend.model.Postit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostItRepository extends JpaRepository<Postit,Long> {
    List<Postit> findAllByBoardId(Long boardId);

}
