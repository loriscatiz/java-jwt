package com.loriscatiz.exception.auth;

public class InvalidAccessTokenException extends UnauthorizedException {
    public final static String DEFAULT_MESSAGE ="Access token is invalid";
    public InvalidAccessTokenException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidAccessTokenException(String message) {
        super(message);
    }

    public InvalidAccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAccessTokenException(Throwable cause) {
        super(cause);
    }
}
