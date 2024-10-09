package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.model.Board;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface IBoardService {
    Set<Board> getAllBoards(Long groupId);

    ResponseEntity<Object> addBoard(Board board, Long groupId);
}