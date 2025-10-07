package com.omni.main.ValidateUserNames.exceptionHandler;

public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException(String message) {
        super(message);
    }
}