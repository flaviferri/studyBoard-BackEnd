package com.sb.studyBoard_Backend.exceptions;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException(String message) {
        super(message);
    }
}
