package com.sb.studyBoard_Backend.interfaces;

import com.sb.studyBoard_Backend.dto.AuthRequest;
import com.sb.studyBoard_Backend.dto.RegisterRequest;

public interface IAuthService {
    String register(RegisterRequest accountDto);

    boolean emailExist(String email);

    String login(AuthRequest authRequest) throws Exception;

    String getAuthenticatedUsername();
}
