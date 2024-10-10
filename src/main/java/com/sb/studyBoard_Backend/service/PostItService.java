package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.exceptions.BoardNotFoundException;
import com.sb.studyBoard_Backend.exceptions.GroupNotFoundException;
import com.sb.studyBoard_Backend.exceptions.NoPostItsOnSelectedDate;
import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.Postit;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.BoardRepository;
import com.sb.studyBoard_Backend.repository.GroupRepository;
import com.sb.studyBoard_Backend.repository.PostItRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostItService implements IPostitService {

    private final PostItRepository postitRepository;
    private final BoardRepository boardRepository;
    private final GroupRepository groupRepository;
    private final AuthService authService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public Postit createPostit(Postit postit, Long boardId) {
        String username = authService.getAuthenticatedUsername();
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar el board por su ID
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("El board no existe."));
        postit.setBoard(board);
        postit.setCreatedBy(user);

        if(postit.getDate() == null) {
            postit.setDate(LocalDate.now());
        }

        return postitRepository.save(postit);
    }

    @Override
    public List<Postit> getAllPostitsByBoardId(Long boardId) {
        return postitRepository.findAllByBoardId(boardId);
    }

    @Override
    public void deletePostit(Long id, Long userId) throws org.springframework.security.access.AccessDeniedException {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Postit postit = postitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postit not found"));

        if (!postit.getCreatedBy().getId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException("No puedes eliminar un post-it que no has creado.");
        }

        if (hasPermission(user, "DELETE_POSTIT")) {
            postitRepository.delete(postit);
        } else {
            throw new org.springframework.security.access.AccessDeniedException("No tienes permiso para eliminar post-its.");
        }
    }

    public boolean hasPermission(UserEntity user, String permissionName) {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissionsEntity().stream())
                .anyMatch(permission -> permission.getName().equals(permissionName));
    }

    @Override
    public ResponseEntity<List<Postit>> getPostItsByDate(Long groupId, LocalDate date) {
        List<Postit> postIts = new ArrayList<>();
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("No se encontró el grupo."));
        Set<Board> boards = group.getBoards();
        for (Board board : boards) {
            List<Postit> boardPostIts = postitRepository.findByBoardAndDate(board, date);
            postIts.addAll(boardPostIts);
        }

        if (postIts.isEmpty()) {
            throw new NoPostItsOnSelectedDate("No hay Post-its para la fecha seleccionada.");
        }

        return new ResponseEntity<>(postIts, HttpStatus.OK);
    }
}