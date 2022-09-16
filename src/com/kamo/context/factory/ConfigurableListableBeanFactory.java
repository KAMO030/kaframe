package com.kamo.context.factory;

import com.kamo.context.BeanDefinition;
import com.kamo.context.exception.NoSuchBeanDefinitionException;

import java.util.List;
import java.util.Set;

public interface ConfigurableListableBeanFactory {
    void registerProcessor(String beanName , BeanDefinition beanDefinition);
    BeanDefinition registerProcessor(Class configClass);

    void register(Class... beanClass);


    List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors();

    Set<BeanInstanceProcessor> getBeanInstanceProcessors();

    Set<BeanPostProcessor> getBeanPostProcessors();


}
