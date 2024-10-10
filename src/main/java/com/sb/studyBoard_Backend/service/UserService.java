package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.interfaces.IUserService;
import com.sb.studyBoard_Backend.model.RoleEntity;
import com.sb.studyBoard_Backend.model.RoleEnum;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.RoleRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService implements IUserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthService authService;

    public UserEntity saveOrUpdateUser(Map<String, Object> userAttributes) {
        String githubId = userAttributes.get("id").toString();
        String name = (String) userAttributes.get("name");
        String email = userAttributes.get("id").toString();
        String avatarUrl = (String) userAttributes.get("avatar_url");

        Optional<UserEntity> existingUser = userRepository.findByGithubId(githubId);

        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            UserEntity user = new UserEntity();
            user.setGithubId(githubId);
            user.setName(name);
            user.setEmail(email);
            user.setAvatarUrl(avatarUrl);
            user.setEnabled(true);
            RoleEntity userRole = roleRepository.findByRoleEnum(RoleEnum.USER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRoles(Collections.singleton(userRole));

            return userRepository.save(user);
        }
    }

    public Optional<UserEntity> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<UserEntity> findByUsername(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity getAuthenticatedUser() {
        String username = authService.getAuthenticatedUsername(); 
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    public UserEntity updateUserAvatar(String avatarUrl) {
        String username = authService.getAuthenticatedUsername();
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        user.setAvatarUrl(avatarUrl);
        return userRepository.save(user);
    }

    public UserEntity updateUserName(String newName) {
        String username = authService.getAuthenticatedUsername();
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        user.setName(newName);
        return userRepository.save(user);
    }
}