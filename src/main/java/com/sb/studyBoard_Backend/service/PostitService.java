package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.model.Postit;
import com.sb.studyBoard_Backend.model.UserEntity;
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

    @Override
    public Postit createPostit(Postit postit, Long userId) throws AccessDeniedException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (hasPermission(user, "CREATE")) {
            return postitRepository.save(postit);
        } else {
            throw new AccessDeniedException("No tienes permiso para crear postits.");
        }
    }

    @Override
    public Postit getPostitById(Long id, Long userId) throws AccessDeniedException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verificar permisos antes de obtener el postit
        if (hasPermission(user, "READ_POSTIT")) {
            return postitRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Postit not found"));
        } else {
            throw new AccessDeniedException("No tienes permiso para leer postits.");
        }
    }
   /* @Override
    public Postit updatePostit(Long id, Postit postit, Long userId) throws AccessDeniedException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verificar permisos antes de actualizar un postit
        if (hasPermission(user, "UPDATE")) {
            Postit existingPostit = getPostitById(id);
            existingPostit.setContent(postit.getContent());
            return postitRepository.save(existingPostit);
        } else {
            throw new AccessDeniedException("No tienes permiso para actualizar postits.");
        }
    }*/

    @Override
    public void deletePostit(Long id, Long userId) throws AccessDeniedException {
        // Buscar el usuario autenticado por su ID
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verificar si el usuario tiene permisos para eliminar el postit
        if (hasPermission(user, "DELETE_POSTIT")) {
            // Verificar que el postit exista antes de eliminarlo
            Postit postit = postitRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Postit not found"));

            // Si todo está correcto, eliminar el postit
            postitRepository.delete(postit);
        } else {
            // Si no tiene permisos, lanzar una excepción de acceso denegado
            throw new AccessDeniedException("No tienes permiso para eliminar postits.");
        }
    }

    // Método privado para verificar permisos
    private boolean hasPermission(UserEntity user, String permissionName) {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> permission.getName().equals(permissionName));
    }

}
