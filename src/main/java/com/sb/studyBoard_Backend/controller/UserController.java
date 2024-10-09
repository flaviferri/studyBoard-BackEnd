package com.sb.studyBoard_Backend.controller;

import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserEntity> getAuthenticatedUser() {
        UserEntity user = userService.getAuthenticatedUser();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me/avatar")
    public ResponseEntity<UserEntity> updateAvatar(@RequestBody Map<String, String> avatarData) {
        String avatarUrl = avatarData.get("avatarUrl");
        UserEntity updatedUser = userService.updateUserAvatar(avatarUrl);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/me/name")
    public ResponseEntity<UserEntity> updateUserName(@RequestBody Map<String, String> requestData) {
        String newName = requestData.get("name");
        UserEntity updatedUser = userService.updateUserName(newName);
        return ResponseEntity.ok(updatedUser);
    }
}
