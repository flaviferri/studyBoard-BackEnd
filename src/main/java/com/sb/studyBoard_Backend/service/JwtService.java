package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.model.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String SECRET_KEY = "sfxN5V7ilY8KNg6oO0EoQSx/P07tz/kINc81+qQ3CFI="; // Cambia esto por una clave secreta segura
    private final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getGithubId()) // O cualquier otro campo que identifique al usuario
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
