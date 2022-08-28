package com.kamo.factory;
@Deprecated
public interface BeanPostProcessor {
    Object postProcessBeforInitialization(String beanName,Object bean);
    Object postProcessAfterInitialization(String beanName,Object bean);
}
