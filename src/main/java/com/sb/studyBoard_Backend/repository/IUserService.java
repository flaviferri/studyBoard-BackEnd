package com.sb.studyBoard_Backend.repository;

import com.sb.studyBoard_Backend.dto.UserDTO;
import com.sb.studyBoard_Backend.exceptions.EmailExistsException;
import com.sb.studyBoard_Backend.model.UserEntity;

public interface IUserService {
    UserEntity registerNewUserAccount(UserDTO accountDto) throws EmailExistsException;
}
