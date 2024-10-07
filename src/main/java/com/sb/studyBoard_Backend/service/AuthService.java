package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.dto.AuthRequest;
import com.sb.studyBoard_Backend.dto.RegisterRequest;
import com.sb.studyBoard_Backend.exceptions.EmailExistsException;
import com.sb.studyBoard_Backend.model.RoleEntity;
import com.sb.studyBoard_Backend.model.RoleEnum;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.RoleRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

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

        // Generar token para el usuario creado
        String token = jwtService.generateToken(userCreated);

        return token;
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public String login(AuthRequest authRequest) throws Exception {
        try {
            // Autenticación del usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            UserEntity userEntity = (UserEntity) userDetails;

            // Generar y devolver el token para el usuario autenticado
            return jwtService.generateToken(userEntity);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials");
        }
    }

    public String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
