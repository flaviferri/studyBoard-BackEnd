package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.model.RoleEntity;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.model.RoleEnum;
import com.sb.studyBoard_Backend.repository.RoleRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserEntity saveOrUpdateUser(Map<String, Object> userAttributes) {
        String githubId = userAttributes.get("id").toString();
        String name = (String) userAttributes.get("name");
        String email = (String) userAttributes.get("email");
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
}