package com.kamo.context.factory;

public interface BeanFactoryPostProcessor extends ApplicationProcessor{
    default void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory){};
}
