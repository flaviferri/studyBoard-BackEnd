package com.sb.studyBoard_Backend.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorObject {
    private String message;
    private int statusCode;
    private Date timestamp;
}
