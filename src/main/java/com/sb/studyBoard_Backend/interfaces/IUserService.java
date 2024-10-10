package com.sb.studyBoard_Backend.interfaces;

import com.sb.studyBoard_Backend.model.UserEntity;

import java.util.Map;
import java.util.Optional;

public interface IUserService {
    UserEntity saveOrUpdateUser(Map<String, Object> userAttributes);

    Optional<UserEntity> findById(Long userId);

    Optional<UserEntity> findByUsername(String email);
}