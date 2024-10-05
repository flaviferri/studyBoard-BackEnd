package com.sb.studyBoard_Backend.dto;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    private String roleEnum;

    private List<PermissionDTO> permissions;
}
