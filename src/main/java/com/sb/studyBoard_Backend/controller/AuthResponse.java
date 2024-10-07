package com.sb.studyBoard_Backend.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class AuthResponse {

    private String token;

    private String error;

    @Builder
    public AuthResponse(String token, String error) {
        this.token = token;
        this.error = error;
    }
}
