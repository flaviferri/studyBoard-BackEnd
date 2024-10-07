package com.sb.studyBoard_Backend.exceptions;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(String message) {
        super(message);
    }
}
