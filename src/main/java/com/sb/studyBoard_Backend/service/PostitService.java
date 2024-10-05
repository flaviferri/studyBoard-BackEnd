package com.sb.studyBoard_Backend.service;


import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Postit;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.BoardRepository;
import com.sb.studyBoard_Backend.repository.PostitRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
    public class PostitService implements IPostitService {

        
        private PostitRepository postitRepository;
        private UserRepository userRepository;
        private BoardRepository boardRepository;

        @Override
        public Postit createPostit(Postit postit, Long userId, Long boardId) {
            // Buscar el usuario que está creando el post-it
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
    
            // Buscar el tablero donde se creará el post-it
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("Board not found"));
    
            // Asignar el usuario creador y el tablero al post-it
            postit.setCreatedBy(user);
            postit.setBoard(board);
    
            // Guardar el post-it en la base de datos
            return postitRepository.save(postit);
        }

        // @Override
        // public Postit createPostit(Postit postit, Long userId, Long boardId) throws AccessDeniedException {
        //     UserEntity user = userRepository.findById(userId)
        //             .orElseThrow(() -> new RuntimeException("User not found"));

        //     Board board = boardRepository.findById(boardId)
        //             .orElseThrow(() -> new RuntimeException("Board not found"));

        //     if (hasPermission(user, "CREATE_POSTIT")) {
        //         postit.setCreatedBy(user);
        //         postit.setBoard(board);
        //         return postitRepository.save(postit);
        //     } else {
        //         throw new AccessDeniedException("No tienes permiso para crear postits.");
        //     }
        // }

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
                    .flatMap(role -> role.getPermissions().stream())
                    .anyMatch(permission -> permission.getName().equals(permissionName));
        }

    }

