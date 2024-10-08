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
        postit.setBoard(board);
        postit.setCreatedBy(user);

        return postitRepository.save(postit);
    }

    @Override
    public List<Postit> getAllPostitsByBoardId(Long boardId) {
        return postitRepository.findAllByBoardId(boardId);
    }
   
    @Override
    public void deletePostit(Long id, Long userId) throws AccessDeniedException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Postit postit = postitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postit not found"));

        if (!postit.getCreatedBy().getId().equals(userId)) {
            throw new AccessDeniedException("No puedes eliminar un post-it que no has creado.");
        }

        if (hasPermission(user, "DELETE_POSTIT")) {
            postitRepository.delete(postit);
        } else {
            throw new AccessDeniedException("No tienes permiso para eliminar post-its.");
        }
    }
  
    public boolean hasPermission(UserEntity user, String permissionName) {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissionsEntity().stream())
                .anyMatch(permission -> permission.getName().equals(permissionName));
    }
}
