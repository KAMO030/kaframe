package com.kamo.context.factory;

import com.kamo.context.BeanDefinition;
import com.kamo.context.exception.NoSuchBeanDefinitionException;

import java.util.List;
import java.util.Set;

public interface ConfigurableListableBeanFactory {
    void registerConfiguration(String beanName ,BeanDefinition beanDefinition);
    BeanDefinition registerConfiguration(Class configClass);

    void register(Class... beanClass);
    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors();

    Set<BeanInstanceProcessor> getBeanInstanceProcessors();

    Set<BeanPostProcessor> getBeanPostProcessors();


}
