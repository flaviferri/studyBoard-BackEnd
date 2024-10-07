package com.sb.studyBoard_Backend.controller;

import com.sb.studyBoard_Backend.model.Postit;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.service.PostitService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/postits")
public class PostitController {

    @Autowired
    private PostitService postitService;

    @PreAuthorize("hasAuthority('CREATE_POSTIT')")
    @PostMapping("/postits")
    public ResponseEntity<Postit> createPostit(@RequestBody Postit postit,
                                               @PathVariable Long boardId,
                                               Authentication authentication) {
        try {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            Long userId = user.getId();

            Postit createdPostit = postitService.createPostit(postit, userId, boardId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPostit);

        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // Obtener un Postit por ID
    @PreAuthorize("hasAuthority('READ_POSTIT')")
    @GetMapping("/{id}")
    public ResponseEntity<Postit> getPostit(@PathVariable Long id, Authentication authentication) {
        try {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            Long userId = user.getId();
            Postit postit = postitService.getPostitById(id, userId);
            return ResponseEntity.ok(postit);
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Eliminar un Postit
    @PreAuthorize("hasAuthority('DELETE_POSTIT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostit(@PathVariable Long id, Authentication authentication) {
        try {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            Long userId = user.getId();
            postitService.deletePostit(id, userId);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
