package com.sb.studyBoard_Backend.controller;

import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BoardController {
    private final BoardService boardService;

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/getAll/{groupId}")
    public Set<Board> getAllBoards(@PathVariable Long groupId) {
        return boardService.getAllBoards(groupId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CREATED')")
    @PostMapping("/add/{groupId}")
    public ResponseEntity<Object> addBoard(@PathVariable Long groupId, @RequestBody Board board) {
        return boardService.addBoard(board, groupId);
    }

}
