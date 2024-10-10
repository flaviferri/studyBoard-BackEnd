package com.sb.studyBoard_Backend.exceptions;

public class NoPostItsOnSelectedDate extends RuntimeException {
    public NoPostItsOnSelectedDate(String message) {
        super(message);
    }
}
