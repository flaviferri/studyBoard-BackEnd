package com.sb.studyBoard_Backend.dto;

import java.util.List;

import com.sb.studyBoard_Backend.model.Board;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private Long id;
    private String groupName;
    private UserDTO createdby;
    private List<Board> boards;
    private List<UserGroupRoleDTO> userGroupRoles;
}
