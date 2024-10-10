package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.exceptions.GroupNotFoundException;
import com.sb.studyBoard_Backend.exceptions.GroupHasNoBoards;
import com.sb.studyBoard_Backend.interfaces.IBoardService;
import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.Postit;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.BoardRepository;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class BoardService implements IBoardService {


    private final BoardRepository boardRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final AuthService authService;
    private final PostItService postItService;

    public Set<Board> getAllBoards(Long groupId) {
        Group group = groupService.getGroupById(groupId).orElseThrow(() ->
                new GroupNotFoundException("El grupo no existe."));
        Set<Board> boards = group.getBoards();
        if (boards.isEmpty()) {
            throw new GroupHasNoBoards("El grupo no tiene ningún board.");
        }
        return boards;
    }

    public ResponseEntity<Object> addBoard(Board board, Long groupId) {
        String username = authService.getAuthenticatedUsername();
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Group group = groupService.getGroupById(groupId).orElseThrow(() ->
                new GroupNotFoundException("El grupo no existe."));
        if (!Objects.equals(group.getCreatedBy().getId(), user.getId())) {
            throw new AccessDeniedException("No tienes permiso para agregar un board en este grupo");
        }
        addInstructionsPostIt(board, user, group, postItService);
        boardRepository.save(board);
        return new ResponseEntity<>(board, HttpStatus.CREATED);
    }

    public static void addInstructionsPostIt(Board board, UserEntity user, Group group, PostItService postItService) {
        board.setGroup(group);
        board.setCreatedBy(user);
        Postit defaultPostit = postItService.createInstructionsPostIt(board);
        defaultPostit.setCreatedBy(user);
        Set<Postit> postits = new HashSet<>();
        postits.add(defaultPostit);
        board.setPostits(postits);
    }
}
