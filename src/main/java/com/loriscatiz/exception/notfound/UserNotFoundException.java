package com.loriscatiz.exception.notfound;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("user not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
