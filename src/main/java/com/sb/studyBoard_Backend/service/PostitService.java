package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Postit;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.BoardRepository;
import com.sb.studyBoard_Backend.repository.PostItRepository;
import com.sb.studyBoard_Backend.exceptions.BoardNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PostItService {

    private final PostItRepository postitRepository;
    private final BoardRepository boardRepository;
    private final AuthService authService;
    private final UserService userService;

    public Postit createPostit(Postit postit, Long boardId) {
        String username = authService.getAuthenticatedUsername();
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar el board por su ID
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("El board no existe."));

        // Asignar el board y el usuario creador al postit
        postit.setBoard(board);
        postit.setCreatedBy(user);

        return postitRepository.save(postit);
    }

}
