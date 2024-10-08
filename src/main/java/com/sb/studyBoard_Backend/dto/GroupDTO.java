package com.sb.studyBoard_Backend.dto;

import java.util.Set;

import lombok.*;

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
}
