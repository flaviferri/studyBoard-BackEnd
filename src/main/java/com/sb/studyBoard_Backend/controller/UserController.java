package com.sb.studyBoard_Backend.controller;


import com.sb.studyBoard_Backend.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@RestController
@RequestMapping("/users")
    public class UserController {

        @Autowired
        private UserServiceImpl userServiceImpl;

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/role/creator/{userId}")
    public ResponseEntity<String> changeRoleToCreator(@PathVariable Long userId) {
        try {
            // Cambia el rol del usuario a Creator
            UserEntity updatedUser = userServiceImpl.changeUserRoleToCreator(userId);
            return ResponseEntity.ok("User role changed to Creator for: " + updatedUser.getUsername());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body("User not found.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("An error occurred while changing the user role.");
        }
    }

    // Cambiar el rol a USER
 /*   @PutMapping("/role/user/{userId}")
    public ResponseEntity<String> changeRoleToUser(@PathVariable Long userId) {
        try {
            UserEntity updatedUser = userServiceImpl.changeUserRoleToUser(userId);
            return ResponseEntity.ok("User role changed to USER for: " + updatedUser.getUsername());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body("User not found.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("An error occurred while changing the user role.");
        }*/
    }


