package com.sb.studyBoard_Backend.repository;

import com.sb.studyBoard_Backend.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByGithubId(String githubId);

}
