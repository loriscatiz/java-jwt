package com.loriscatiz.exception.badrequest;

public class UsernameAlreadyPresentException extends BadRequestException {
    public UsernameAlreadyPresentException() {
        super("username is already taken");
    }

    public UsernameAlreadyPresentException(String message) {
        super(message);
    }

    public UsernameAlreadyPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyPresentException(Throwable cause) {
        super(cause);
    }
}
