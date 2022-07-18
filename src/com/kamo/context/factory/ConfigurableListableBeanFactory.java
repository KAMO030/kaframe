package com.kamo.context.factory;

import com.kamo.context.BeanDefinition;
import com.kamo.context.exception.NoSuchBeanDefinitionException;

public interface ConfigurableListableBeanFactory {
    void registerConfiguration(Object regist);
    String[] getBeanDefinitionNames();
    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;
}
