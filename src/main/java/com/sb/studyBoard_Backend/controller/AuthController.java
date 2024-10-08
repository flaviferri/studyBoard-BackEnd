package com.sb.studyBoard_Backend.controller;

import com.sb.studyBoard_Backend.dto.AuthRequest;
import com.sb.studyBoard_Backend.dto.RegisterRequest;
import com.sb.studyBoard_Backend.exceptions.EmailExistsException;
import com.sb.studyBoard_Backend.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        try {
            String token = authService.register(registerRequest);
            AuthResponse response = AuthResponse.builder().token(token).build();

            return ResponseEntity.ok(response);
        } catch (EmailExistsException e) {
            return ResponseEntity.badRequest().body(AuthResponse.builder().error(e.getMessage()).build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            String token = authService.login(authRequest);
            AuthResponse response = AuthResponse.builder().token(token).build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder().error(e.getMessage()).build());
        }
    }
}
