package com.sb.studyBoard_Backend.controller;

import com.sb.studyBoard_Backend.model.Postit;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.service.PostItService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/postits")
public class PostItController {

    @Autowired
    private PostItService postitService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/{boardId}")
    public ResponseEntity<Postit> createPostit(@RequestBody Postit postit,
                                               @PathVariable Long boardId,
                                               Authentication authentication) {
        try {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            Long userId = user.getId();
            Postit createdPostit = postitService.createPostit(postit, boardId);
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


    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/date/{groupId}")
    public ResponseEntity<List<Postit>> getAllPostitsByDate(@PathVariable Long groupId, @RequestParam LocalDate date) {
        return postitService.getPostItsByDate(groupId, date);
    }
}
