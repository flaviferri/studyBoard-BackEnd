package com.sb.studyBoard_Backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.studyBoard_Backend.model.Postit;
import com.sb.studyBoard_Backend.service.PostItService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/postit")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostItController {
    private final PostItService postitService;

    @PostMapping("/{boardId}/add")
    public ResponseEntity<Postit> createPostit(@PathVariable Long boardId, @RequestBody Postit postit) {
        Postit createdPostit = postitService.createPostit(postit, boardId);
        return new ResponseEntity<>(createdPostit, HttpStatus.CREATED);
    }
}
