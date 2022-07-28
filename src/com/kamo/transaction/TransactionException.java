package com.kamo.transaction;

public class TransactionException extends RuntimeException {
    public TransactionException() {
        super();
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }

    protected TransactionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TransactionException(String message) {
        super(message);
    }
}
