package com.sb.studyBoard_Backend.controller;

import com.sb.studyBoard_Backend.model.Postit;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.service.PostitService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/{boardId}")
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<Postit>> getAllPostitsByBoardId(@PathVariable Long boardId) {
        try {
            List<Postit> postits = postitService.getAllPostitsByBoardId(boardId);
            return ResponseEntity.ok(postits);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
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
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

