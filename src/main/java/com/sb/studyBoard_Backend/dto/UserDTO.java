package com.sb.studyBoard_Backend.dto;

import java.util.List;


import lombok.*;

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
