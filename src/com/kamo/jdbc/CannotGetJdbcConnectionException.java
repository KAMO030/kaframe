package com.kamo.jdbc;

public class CannotGetJdbcConnectionException extends RuntimeException {
    public CannotGetJdbcConnectionException( ) {
        super();
    }
    public CannotGetJdbcConnectionException(String message) {
        super(message);
    }
    public CannotGetJdbcConnectionException(String message,Throwable cause) {
        super(message,cause);
    }
}
