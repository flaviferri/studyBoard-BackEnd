package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.dto.RegisterRequest;
import com.sb.studyBoard_Backend.exceptions.EmailExistsException;
import com.sb.studyBoard_Backend.model.UserEntity;

public interface IUserService {
    UserEntity registerNewUserAccount(RegisterRequest registerRequest) throws EmailExistsException;
}

