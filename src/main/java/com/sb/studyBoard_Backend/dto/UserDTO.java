package com.sb.studyBoard_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private Long id;
    private String name;
    private String email;
    private String github;
    private String avatarUrl;
    private boolean enabled;

    private List<RoleDTO> roles;

}
