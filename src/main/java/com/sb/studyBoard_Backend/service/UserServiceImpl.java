package com.sb.studyBoard_Backend.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sb.studyBoard_Backend.dto.UserDTO;
import com.sb.studyBoard_Backend.exceptions.EmailExistsException;
import com.sb.studyBoard_Backend.model.Role;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.RoleRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserEntity registerNewUserAccount(UserDTO accountDto) throws EmailExistsException {

        if (emailExist(accountDto.getEmail())) {

            throw new EmailExistsException("There is an account with that email address:" + accountDto.getEmail());
        }

        Optional<Role> defaultRole = roleRepository.findByName("ROLE_USER");

        if (!defaultRole.isPresent()) {
            throw new RuntimeException("Default role not found!");
        }

        UserEntity user = UserEntity.builder()
                .name(accountDto.getName())
                .email(accountDto.getEmail())
                .password(passwordEncoder.encode(accountDto.getPassword()))
                .enabled(true)
                .roles(Arrays.asList(defaultRole.get()))
                .build();

        return userRepository.save(user);
    }

    private boolean emailExist(String email) {

        return userRepository.findByEmail(email).isPresent();
    }
}
