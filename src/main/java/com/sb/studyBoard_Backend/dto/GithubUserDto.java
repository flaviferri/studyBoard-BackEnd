package com.sb.studyBoard_Backend.dto;


import lombok.Data;

@Data
public class GithubUserDto {
    private String githubId;
    private String name;
    private String email;
    private String avatarUrl; // Si necesitas más campos, puedes agregarlos aquí
}