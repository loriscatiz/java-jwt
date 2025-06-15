package com.loriscatiz.exception.badrequest;

public class InvalidRequestException extends BadRequestException {
    public InvalidRequestException() {
        super("invalid request");
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }
}
