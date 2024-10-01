package com.sb.studyBoard_Backend.controller;

import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/{groupId}/getAll")
    public List<Board> getAllBoards(@PathVariable int groupId) {
        return boardService.getAllBoards(groupId);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addBoard(@RequestBody Board board) {
        return boardService.addBoard(board);
    }

}
