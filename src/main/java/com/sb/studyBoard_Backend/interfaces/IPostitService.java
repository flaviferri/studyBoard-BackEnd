package com.sb.studyBoard_Backend.interfaces;

import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Postit;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

public interface IPostitService {

    Postit createPostit(Postit postit, Long boardId) throws AccessDeniedException;

    List<Postit> getAllPostitsByBoardId(Long boardId) throws AccessDeniedException;

    void deletePostit(Long id, Long userId) throws AccessDeniedException;

    ResponseEntity<List<Postit>> getPostItsByDate(Long groupId, LocalDate date);

    Postit createInstructionsPostIt(Board board);
}


