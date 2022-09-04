package com.kamo.util.exception;

public class EmptyWalletException extends RuntimeException {
    public EmptyWalletException() {
        super("KFC Crazy Thursday need 50￥.");
    }
}
