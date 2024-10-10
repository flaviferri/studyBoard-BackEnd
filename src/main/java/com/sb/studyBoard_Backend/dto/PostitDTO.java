package com.sb.studyBoard_Backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostitDTO {
    private Long id;
    private String color;
    private String title;
    private LocalDate date;
    private String textContent;
    private Boolean isOwner;
}
