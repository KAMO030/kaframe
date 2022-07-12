package kamo.factory;

public interface BeanPostProcessor {
    Object postProcessBeforInitialization(String beanName,Object bean);
    Object postProcessAfterInitialization(String beanName,Object bean);
}
