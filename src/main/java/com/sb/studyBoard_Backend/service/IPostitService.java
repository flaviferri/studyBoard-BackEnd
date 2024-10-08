package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.model.Postit;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface IPostitService {

    Postit createPostit(Postit postit, Long userId, Long boardId) throws AccessDeniedException;

    List<Postit> getAllPostitsByBoardId(Long boardId) throws AccessDeniedException;

    Postit getPostitById(Long id, Long userId) throws AccessDeniedException; // Agregar userId
    /*
     * Postit updatePostit(Long id, Postit postit, Long userId) throws
     * AccessDeniedException;
     */

    void deletePostit(Long id, Long userId) throws AccessDeniedException;

}