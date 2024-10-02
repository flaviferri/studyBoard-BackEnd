package com.sb.studyBoard_Backend.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.sb.studyBoard_Backend.model.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sb.studyBoard_Backend.dto.UserDTO;
import com.sb.studyBoard_Backend.exceptions.EmailExistsException;
import com.sb.studyBoard_Backend.model.RoleEntity;
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

        Optional<RoleEntity> defaultRole = roleRepository.findByRoleEnum(RoleEnum.USER);

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

    public UserEntity changeUserRoleToCreator(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Busca el rol en la base de datos usando RoleEnum
        RoleEntity creatorRole = roleRepository.findByRoleEnum(RoleEnum.CREATOR)
                .orElseThrow(() -> new RuntimeException("Creator role not found"));

        // Agrega el rol al usuario
        user.getRoles().add(creatorRole);

        return userRepository.save(user);
    }
}

  /*  public UserEntity changeUserRoleToUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Busca el rol USER en la base de datos
        RoleEntity userRole = roleRepository.findByRoleEnum(RoleEnum.USER)
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        // Obtiene los roles actuales del usuario
        Set<RoleEntity> roles = new HashSet<>(user.getRoles());

        // Agrega el rol USER solo si no lo tiene ya
        roles.add(userRole);

        user.setRoles(roles);

        return userRepository.save(user);  // Guarda los cambios en la base de datos
    }*/

