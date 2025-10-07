package com.omni.main.ValidateUserNames.exceptionHandler;

public class UsernameHistoryViolationException extends RuntimeException {
    public UsernameHistoryViolationException(String message) {
        super(message);
    }
}