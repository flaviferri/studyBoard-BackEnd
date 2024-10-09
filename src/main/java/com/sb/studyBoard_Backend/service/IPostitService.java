package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.exceptions.BoardNotFoundException;
import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Postit;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.BoardRepository;
import com.sb.studyBoard_Backend.repository.PostItRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

public interface IPostitService {

    Postit createPostit(Postit postit, Long boardId) throws AccessDeniedException;

    List<Postit> getAllPostitsByBoardId(Long boardId) throws AccessDeniedException;

    void deletePostit(Long id, Long userId) throws AccessDeniedException;

    ResponseEntity<List<Postit>> getPostItsByDate(Long groupId, LocalDate date);
}


