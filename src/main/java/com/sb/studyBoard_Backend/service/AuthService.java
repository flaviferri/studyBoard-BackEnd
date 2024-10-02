package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.dto.AuthRequest;
import com.sb.studyBoard_Backend.model.UserEntity;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserServiceImpl userService;

    public String login(AuthRequest authRequest) throws Exception {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            UserEntity userEntity = (UserEntity) userDetails;

            return jwtService.generateToken(userEntity);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials");
        }
    }
}
