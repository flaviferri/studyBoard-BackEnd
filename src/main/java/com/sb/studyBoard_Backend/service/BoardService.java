package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.exceptions.GroupNotFoundException;
import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> getAllBoards(int groupId) {

    }

    public ResponseEntity<Object> addBoard(Board board) {
        Group group = groupService.findByID(board.getGroup()).orElseThrow(() ->
                new GroupNotFoundException("El grupo no existe."));

    }
}
