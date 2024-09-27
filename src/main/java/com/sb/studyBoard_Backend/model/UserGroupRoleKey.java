package com.sb.studyBoard_Backend.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserGroupRoleKey implements Serializable {

    private Long userId;
    private Long groupId;
    private Long roleId;
}
