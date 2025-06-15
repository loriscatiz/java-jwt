package com.loriscatiz.exception.auth;

public class InvalidCredentialsException extends UnauthorizedException {
    public InvalidCredentialsException() {
        super("invalid credentials");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCredentialsException(Throwable cause) {
        super(cause);
    }
}
