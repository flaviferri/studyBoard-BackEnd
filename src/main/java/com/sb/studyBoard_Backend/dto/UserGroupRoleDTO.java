package com.sb.studyBoard_Backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupRoleDTO {
    private Long id;
    private UserDTO user;
    private GroupDTO group;
    private RoleDTO role;
}
