package com.sb.studyBoard_Backend.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AuthRequest {

    private String email;
    private String password;
}