package com.codegym.web.exseptions;

public class IllegalUserIdException extends RuntimeException {
    public IllegalUserIdException() {
        super("User with this ID not found");
    }
}
