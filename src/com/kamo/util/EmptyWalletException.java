package com.kamo.util;

public class EmptyWalletException extends RuntimeException {
    public EmptyWalletException() {
        super("KFC Crazy Thursday need 50￥.");
    }
}
