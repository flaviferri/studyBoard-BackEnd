package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Postit;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.BoardRepository;
import com.sb.studyBoard_Backend.repository.PostitRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class PostitService implements IPostitService {

    @Autowired
    private PostitRepository postitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public Postit createPostit(Postit postit, Long userId, Long boardId) throws AccessDeniedException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        if (hasPermission(user, "CREATE_POSTIT")) {
            postit.setCreatedBy(user);
            postit.setBoard(board);
            return postitRepository.save(postit);
        } else {
            throw new AccessDeniedException("No tienes permiso para crear postits.");
        }
    }

    @Override
    public Postit getPostitById(Long id, Long userId) throws AccessDeniedException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (hasPermission(user, "READ_POSTIT")) {
            return postitRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Postit not found"));
        } else {
            throw new AccessDeniedException("No tienes permiso para leer postits.");
        }
    }


    @Override
    public void deletePostit(Long id, Long userId) throws AccessDeniedException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (hasPermission(user, "DELETE_POSTIT")) {

            Postit postit = postitRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Postit not found"));

            postitRepository.delete(postit);
        } else {
            throw new AccessDeniedException("No tienes permiso para eliminar postits.");
        }
    }

    public boolean hasPermission(UserEntity user, String permissionName) {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissionsEntity().stream())
                .anyMatch(permission -> permission.getName().equals(permissionName));
    }

}
