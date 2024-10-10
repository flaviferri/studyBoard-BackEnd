package com.sb.studyBoard_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private Long id;
    private String groupName;
    private CreatedByDTO createdBy;
    private Set<BoardDTO> boards;
    private Boolean isCreator;
    private Boolean isMember;
}
