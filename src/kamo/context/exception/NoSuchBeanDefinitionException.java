package kamo.context.exception;

public class NoSuchBeanDefinitionException extends BeansException {
    public NoSuchBeanDefinitionException() {
    }
    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }
}
