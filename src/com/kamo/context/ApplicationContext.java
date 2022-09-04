package com.kamo.context;

import com.kamo.context.factory.ConfigurableListableBeanFactory;
import com.kamo.context.listener.ApplicationEventPublisher;


public interface ApplicationContext extends ApplicationEventPublisher,BeanDefinitionRegistry,BeanFactory, ConfigurableListableBeanFactory {
    default String getApplicationContextName(){
        return this.getClass().getName();
    }

    void destroy();
}
