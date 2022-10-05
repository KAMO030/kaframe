package com.kamo.context.exception;

public class BeanDefinitionStoreException extends BeansException {
    public BeanDefinitionStoreException(String message) {
        super(message);
    }
    public BeanDefinitionStoreException() {
        super();
    }

    public BeanDefinitionStoreException(String message, BeanDefinitionStoreException e) {
        super(message, e);
    }
}
