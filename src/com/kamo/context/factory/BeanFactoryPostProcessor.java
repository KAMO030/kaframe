package com.kamo.context.factory;

public interface BeanFactoryPostProcessor {
    default void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory){};
}
