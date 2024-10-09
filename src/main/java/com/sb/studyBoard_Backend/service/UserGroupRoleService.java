package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.sb.studyBoard_Backend.model.UserGroupRole;
import com.sb.studyBoard_Backend.repository.UserGroupRoleRepository;

import lombok.AllArgsConstructor;


@Service
public class UserGroupRoleService {

    private final UserGroupRoleRepository userGroupRoleRepository;
    private final UserRepository userRepository; // Añade el repositorio de usuario

    public UserGroupRoleService(UserGroupRoleRepository userGroupRoleRepository, UserRepository userRepository) {
        this.userGroupRoleRepository = userGroupRoleRepository;
        this.userRepository = userRepository; // Inicializa el repositorio de usuario
    }

    public UserGroupRole save(UserGroupRole userGroupRole) {
        return userGroupRoleRepository.save(userGroupRole);
    }

    // Método para guardar o actualizar el usuario
    public void saveUser(UserEntity user) {
        userRepository.save(user); // Guarda el usuario en la base de datos
    }
}