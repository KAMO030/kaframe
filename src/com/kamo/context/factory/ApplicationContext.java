package com.kamo.context.factory;

import com.kamo.context.listener.ApplicationEventPublisher;


public interface ApplicationContext extends ApplicationEventPublisher,BeanDefinitionRegistry,BeanFactory, ConfigurableListableBeanFactory {
    default String getApplicationContextName(){
        return this.getClass().getName();
    }
    void register(Class... beanClass);

}
