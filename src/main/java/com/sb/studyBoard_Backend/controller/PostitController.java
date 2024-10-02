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

public class PostitController {

    @Autowired
    private PostitService postitService;

    @PreAuthorize("hasAuthority('CREATE_POSTIT')")
    @PostMapping
    public ResponseEntity<Postit> createPostit(@RequestBody Postit postit, Authentication authentication) {
        try {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            Long userId = user.getId();
            Postit createdPostit = postitService.createPostit(postit, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPostit);

        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @SneakyThrows
    @PreAuthorize("hasAuthority('READ_POSTIT')")
    @GetMapping("/{id}")
    public ResponseEntity<Postit> getPostit(@PathVariable Long id, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Long userId= user.getId();
        Postit postit = postitService.getPostitById(id,userId);
        return ResponseEntity.ok(postit);
    }

/*    @PreAuthorize("hasAuthority('UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<Postit> updatePostit(@PathVariable Long id, @RequestBody Postit postit) {
        Postit updatedPostit = postitService.updatePostit(id, postit);
        return ResponseEntity.ok(updatedPostit);
    }*/

    @SneakyThrows
    @PreAuthorize("hasAuthority('DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostit(@PathVariable Long id, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Long userId= user.getId();
        postitService.deletePostit(id,userId);
        return ResponseEntity.noContent().build();
    }
}
