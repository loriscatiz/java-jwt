package com.loriscatiz.exception.auth;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("forbidden");
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenException(Throwable cause) {
        super(cause);
    }
}
