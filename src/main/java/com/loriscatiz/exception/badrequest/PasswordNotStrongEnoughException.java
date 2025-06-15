package com.loriscatiz.exception.badrequest;

public class PasswordNotStrongEnoughException extends BadRequestException {
    public PasswordNotStrongEnoughException() {
        super("password is not strong enough");
    }

    public PasswordNotStrongEnoughException(String message) {
        super(message);
    }

    public PasswordNotStrongEnoughException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordNotStrongEnoughException(Throwable cause) {
        super(cause);
    }
}
