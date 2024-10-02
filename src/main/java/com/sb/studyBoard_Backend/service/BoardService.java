package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.exceptions.GroupNotFoundException;
import com.sb.studyBoard_Backend.exceptions.GroupHasNoBoards;
import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.BoardRepository;
import com.sb.studyBoard_Backend.repository.GroupRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final GroupService groupService;
    private final UserRepository userRepository;

    public Set<Board> getAllBoards(Long groupId, Long userId) {
        Group group = groupService.findByID(groupId).orElseThrow(() ->
                new GroupNotFoundException("El grupo no existe."));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        /*   Esto descomentar si al final solo los miembros pueden ver el grupo
        if (!user.getGroups().contains(group)) {
            throw new AccessDeniedException("No tienes permiso para acceder a este grupo");
        }*/

        Set<Board> boards = group.getBoards();

        if (boards.isEmpty()) {
            throw new GroupHasNoBoards("El grupo no tiene ningún board.");
        }

        return boards;
    }

    public ResponseEntity<Object> addBoard(Board board) {
        UserEntity user = userRepository.findById(board.getCreatedBy().getId())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Group group = groupService.findByID(board.getGroup()).orElseThrow(() ->
                new GroupNotFoundException("El grupo no existe."));
        if (!Objects.equals(group.getCreatedBy().getId(), user.getId())) {
            throw new AccessDeniedException("No tienes permiso para agregar un board en este grupo");
        }

        boardRepository.save(board);
        return new ResponseEntity<>(board, HttpStatus.CREATED);
    }
}
