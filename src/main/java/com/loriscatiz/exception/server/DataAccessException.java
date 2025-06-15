package com.loriscatiz.exception.server;


public class DataAccessException extends ServerErrorException {
    public DataAccessException() {
        super("something wrong happened at the database layer");
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }
}
