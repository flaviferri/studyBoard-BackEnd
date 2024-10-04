package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.dto.AuthRequest;
import com.sb.studyBoard_Backend.dto.GithubUserDto;
import com.sb.studyBoard_Backend.dto.RegisterRequest;
import com.sb.studyBoard_Backend.exceptions.EmailExistsException;
import com.sb.studyBoard_Backend.model.RoleEntity;
import com.sb.studyBoard_Backend.model.RoleEnum;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.RoleRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;

import lombok.AllArgsConstructor;
import java.util.Arrays;

import java.util.Collections;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public String register(RegisterRequest accountDto) throws EmailExistsException {
        if (emailExist(accountDto.getEmail())) {
            throw new EmailExistsException("There is an account with that email address:" + accountDto.getEmail());
        }

        Optional<RoleEntity> defaultRole = roleRepository.findByRoleEnum(RoleEnum.USER);
        if (!defaultRole.isPresent()) {
            throw new RuntimeException("Default role not found!");
        }

        // Crear y guardar el usuario
        UserEntity user = UserEntity.builder()
                .name(accountDto.getName())
                .email(accountDto.getEmail())
                .password(passwordEncoder.encode(accountDto.getPassword()))
                .enabled(true)
                .roles(Arrays.asList(defaultRole.get()))
                .build();

        UserEntity userCreated = userRepository.save(user);

        // // Autenticar al usuario automáticamente
        // authenticationManager.authenticate(
        // new UsernamePasswordAuthenticationToken(
        // accountDto.getEmail(),
        // accountDto.getPassword()));

        String token = jwtService.generateToken(userCreated);

        return token;
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

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

 /*   public String loginWithGithub(GithubUserDto githubUserDto) {
        Optional<UserEntity> optionalUser = userRepository.findByGithubId(githubUserDto.getGithubId());

        UserEntity user;
        if (optionalUser.isPresent()) {
            // El usuario ya existe, lo cargamos
            user = optionalUser.get();
        } else {
            // El usuario no existe, lo creamos y le asignamos el rol USER
            RoleEntity userRole = roleRepository.findByRoleEnum(RoleEnum.USER)
                    .orElseThrow(() -> new RuntimeException("Role USER not found!"));

            user = new UserEntity();
            user.setGithubId(githubUserDto.getGithubId());
            user.setName(githubUserDto.getName());
            user.setEmail(githubUserDto.getEmail());
            user.setAvatarUrl(githubUserDto.getAvatarUrl());
            user.setEnabled(true);

            // Asignar el rol USER
            user.setRoles(Collections.singletonList(userRole));

            // Guardar el usuario en la base de datos
            user = userRepository.save(user);
        }

        // Generar un token JWT para el usuario autenticado
        return jwtService.generateToken(user);
    }*/
}