package com.sb.studyBoard_Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<com.sb.studyBoard_Backend.model.User> findByGithubId(String githubId);

}
