package com.kamo.context.factory;

import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.exception.BeansException;

public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor{
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;
}
