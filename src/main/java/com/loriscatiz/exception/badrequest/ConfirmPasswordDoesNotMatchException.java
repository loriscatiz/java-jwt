package com.loriscatiz.exception.badrequest;

public class ConfirmPasswordDoesNotMatchException extends BadRequestException {
    public ConfirmPasswordDoesNotMatchException() {
        super("password not matching");
    }

    public ConfirmPasswordDoesNotMatchException(String message) {
        super(message);
    }

    public ConfirmPasswordDoesNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfirmPasswordDoesNotMatchException(Throwable cause) {
        super(cause);
    }
}
